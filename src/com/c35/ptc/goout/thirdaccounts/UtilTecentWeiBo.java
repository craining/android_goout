package com.c35.ptc.goout.thirdaccounts;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.interfaces.ThirdAccountTokenListener;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.StringUtil;
import com.c35.ptc.goout.util.TimeUtil;

/**
 * 
 * API文档详见: http://wiki.open.t.qq.com/index.php/API%E6%96%87%E6%A1%A3
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-10
 */

public class UtilTecentWeiBo {

	/**
	 * 获取腾讯微博账号token
	 * 
	 * 详见：http://wiki.open.t.qq.com/index.php/OAuth2.0%E9%89%B4%E6%9D%83
	 * 
	 * 2.Implicit grant
	 * 
	 * @Description:
	 * @param context
	 * @param code
	 * @param openid
	 * @param openkey
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-10
	 */
	public static void getTecentWeiBoToken(Context context, String code, String openid, String openkey, final ThirdAccountTokenListener listener) {

		String access_url = "https://open.t.qq.com/cgi-bin/oauth2/access_token";
		HttpClient request = ThirdHttpUtil.getNewHttpClient();
		HttpPost httpPost;
		try {
			httpPost = new HttpPost(new URI(access_url));
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("client_id", ThirdConfigConstants.QQWEIBO_API_KEY));
			params.add(new BasicNameValuePair("client_secret", ThirdConfigConstants.QQWEIBO_SECRET_KEY));
			params.add(new BasicNameValuePair("grant_type", "authorization_code"));
			params.add(new BasicNameValuePair("redirect_uri", ThirdConfigConstants.QQWEIBO_OAUTH_CALLBACK));
			params.add(new BasicNameValuePair("code", code));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			HttpResponse resposne = request.execute(httpPost);
			if (200 == resposne.getStatusLine().getStatusCode()) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ThirdHttpUtil.readStream(inputStream));

				GoOutDebug.e("getTecentWeiBoToken", "result=" + result);

				// TODO 截取方式可能有问题，后期优化：先split("&"), 再split("=")
				String access_token = result.substring(result.indexOf("access_token=") + 13, result.indexOf("&expires_in="));
				String expires_in = result.substring(result.indexOf("&expires_in=") + 12, result.indexOf("&refresh_token="));
				String open_id = result.substring(result.indexOf("&openid=") + 8, result.indexOf("&name="));
				String nickName = result.substring(result.indexOf("&nick=") + 6, result.length());

				GoOutDebug.e("getTecentWeiBoToken", "access_token=" + access_token + "  expires_in=" + expires_in + "   open_id=" + open_id);
				/**
				 * result=access_token=1585fed123ce7b028d6aa0cf535f5118&expires_in=604800&refresh_token=
				 * d0b57caa6ef95231245927afc3475318
				 * &openid=47746dca89dd6c5cd881c4b762083f7f&name=craining1989&nick=庄广钰
				 */

				/**
				 * result=access_token=047a79c01ee0238913fedc4939c8133b&expires_in=604800&refresh_token=8322
				 * a9afb744c755ccfdf0739ae556ae
				 * &openid=62c689949ddc88fe034250ce441be0fe&name=ydobyreve&nick=everybody
				 */

				Account account = new Account();
				account.setAccessToken(access_token);
				account.setExpiresIn(expires_in);
				account.setThirdId(open_id);
				account.setAccountName(nickName);
				account.setThirdId(open_id);
				account.setAccountType(Account.TYPE_QQWEIBO);

				if (!StringUtil.isNull(account.getThirdId()) && !StringUtil.isNull(account.getAccountName())) {
					listener.getTokenSuccess(account);
					return;
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		listener.getTokenFail();
	}

	/**
	 * 获得用户信息
	 * 
	 * @Description:
	 * @param account
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	public static void getMoreUserInfo(Account account, Context context) {

		try {
			String url = "https://open.t.qq.com/api/user/info";
			GoOutDebug.e("getMoreUserInfo tencent", "request url=" + url);
			HttpClient request = ThirdHttpUtil.getNewHttpClient();
			HttpPost httpPost = new HttpPost(new URI(url));
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			// format=json&appid=xx&openid=xx&openkey=xx&clientip=xx&reqtime=xx&sig=xx&wbversion=1
			params.add(new BasicNameValuePair("access_token", account.getAccessToken()));
			params.add(new BasicNameValuePair("oauth_consumer_key", ThirdConfigConstants.QQWEIBO_API_KEY));
			params.add(new BasicNameValuePair("openid", account.getThirdId()));
			params.add(new BasicNameValuePair("clientip", NetworkUtil.getIp(context)));// 用户ip（必须正确填写用户侧真实ip，不能为内网ip及以127或255开头的ip，以分析用户所在地）
			params.add(new BasicNameValuePair("format", "json"));
			params.add(new BasicNameValuePair("oauth_version", "2.a"));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse resposne = request.execute(httpPost);
			if (200 == resposne.getStatusLine().getStatusCode()) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ThirdHttpUtil.readStream(inputStream));
				GoOutDebug.e("getMoreUserInfo tencent", "result=" + result);

				JSONObject object = new JSONObject(result);

				if (object.has("data")) {
					JSONObject dataObj = object.getJSONObject("data");

					account.setThirdId(dataObj.getString("seqid"));
					account.setLogo(object.getString("profile_image_url"));
				}
				// account.set
				/**
				 * {"data": {"birth_day":1, "birth_month":1, "birth_year":2010, "city_code":"HEA",
				 * "comp":null, "country_code":"AFG", "edu":null,"email":"",
				 * "exp":25,"fansnum":3,"favnum":0,"head":"", "homecity_code":"","homecountry_code":"",
				 * "homepage":"","homeprovince_code":"", "hometown_code":"","https_head":"","idolnum":21,
				 * "industry_code":0,"introduction":"","isent":0,"ismyblack":0,
				 * "ismyfans":0,"ismyidol":0,"isrealname":1,"isvip":0,"level":1,
				 * "location":"阿富汗","mutual_fans_num":3,"name":"ydobyreve",
				 * "nick":"everybody","openid":"62C689949DDC88FE034250CE441BE0FE",
				 * "province_code":"","regtime":1333779659,"send_private_flag":2,"sex":1,
				 * "tag":null,"tweetnum":1,"verifyinfo":""},"errcode":0,"msg":"ok","ret":0,
				 * "seqid":5869488008777332696}
				 */
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发布一条动态
	 * 
	 * 
	 * 详见：http://wiki.open.t.qq.com/index.php/API%E6%96%87%E6%A1%A3/%E5%BE%AE%E5%8D%9A%E6%
	 * 8E%A5%E5%8F%A3/%E5%8F%91%E8%A1%A8%E4%B8%80%E6%9D%A1%E5%BE%AE%E5%8D%9A%E4%BF%A1%E6%81%AF
	 * 
	 * @Description:
	 * @param token
	 * @param openid
	 * @param content
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-10
	 */
	public static boolean updateOneStatues(Context context, String token, String openid, String content) {

		try {
			String url = "https://open.t.qq.com/api/t/add";
			GoOutDebug.e("updateOneStatues tencent", "request url=" + url);
			HttpClient request = ThirdHttpUtil.getNewHttpClient();
			HttpPost httpPost = new HttpPost(new URI(url));
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("access_token", token));
			params.add(new BasicNameValuePair("oauth_consumer_key", ThirdConfigConstants.QQWEIBO_API_KEY));
			params.add(new BasicNameValuePair("openid", openid));
			params.add(new BasicNameValuePair("clientip", NetworkUtil.getIp(context)));// 用户ip（必须正确填写用户侧真实ip，不能为内网ip及以127或255开头的ip，以分析用户所在地）
			params.add(new BasicNameValuePair("format", "json"));
			params.add(new BasicNameValuePair("content", content));// 微博内容（若在此处@好友，需正确填写好友的微博账号，而非昵称），不超过140字
			params.add(new BasicNameValuePair("syncflag", "1"));// 微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0），目前仅支持oauth1.0鉴权方式
			params.add(new BasicNameValuePair("oauth_version", "2.a"));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse resposne = request.execute(httpPost);
			if (200 == resposne.getStatusLine().getStatusCode()) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ThirdHttpUtil.readStream(inputStream));
				GoOutDebug.e("updateOneStatues tencent", "result=" + result);

				JSONObject obj = new JSONObject(result);

				String errorCode = obj.getString("errcode");
				String ret = obj.getString("ret");

				if (errorCode.equals("0") && ret.equals("0")) {
					if (obj.getString("msg").equals("ok")) {
						return true;
					}
				} else if (ret.equals("3")) {
					/**
					 * 34 appkey和accesstoken不匹配
					 * 
					 * 36 accesstoken非法 37 accesstoken过期 38 accesstoken被废弃
					 * 
					 * 40 accesstoken长度非法
					 */
					if (errorCode.equals("37") || errorCode.equals("36") || errorCode.equals("38") || errorCode.equals("40") || errorCode.equals("34")) {
						// access token 有问题
						// 详见：http://wiki.open.t.qq.com/index.php/%E8%BF%94%E5%9B%9E%E9%94%99%E8%AF%AF%E7%A0%81%E8%AF%B4%E6%98%8E
						GoOutController.getInstance().deleteAccountToken(context, Account.TYPE_QQWEIBO);
					}
				}

				/**
				 * 正确的json返回结果：
				 * 
				 * { errcode : 0, msg : ok, ret : 0, data : { id : xxx, timestamp : xxx }, seqid : xxx }
				 * 错误的json返回结果：
				 * 
				 * {ret:"1",errcode:"1",msg:"error clientip"}
				 */
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**************** 以下方法暂时未用 *****************/

	// /**
	// * 判断token是否可用
	// *
	// * 尚未发现可用的直接验证的api，官方的说法是获取token时，同时返回有效期，保存有该效期，供应用自己判断。
	// *
	// * @Description:
	// * @param token
	// * @return true可用，false过期
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-9
	// */
	// public static boolean isTokenEnabled(String expiresIn, long requestTime) {
	//
	// // 此方法是获取保存的有效期和请求时间，计算是否仍有效
	//
	// if (TimeUtil.getCurrentTimeMillis() - requestTime > (Long.parseLong(expiresIn) - 60)) {
	// GoOutDebug.e("isTencentWeiBoTokenEnabled", "已过期");
	// // TODO 刷新token
	//
	// return false;
	// } else {
	// GoOutDebug.e("isTencentWeiBoTokenEnabled", "未过期");
	// return true;
	// }
	// }
	//
	// /**
	// * 取消腾讯微博授权
	// *
	// * @Description:
	// * @param context
	// * @param token
	// * @param openid
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-10
	// */
	// public static boolean disableToken(Context context, String token, String openid) {
	//
	// try {
	// StringBuffer buffer = new StringBuffer();
	// String url = "http://open.t.qq.com/api/auth/revoke_auth";
	// // 拼url
	// buffer.append("?format=json").append("&oauth_consumer_key=").append(ThirdConfigConstants.QQWEIBO_API_KEY).append("&access_token=").append(token).append("&openid=").append(openid).append("&clientip=").append(NetworkUtil.getIp(context)).append("&oauth_version=2.a");
	// HttpParams httpParams = new BasicHttpParams();
	// // 设置超时时间
	// HttpConnectionParams.setConnectionTimeout(httpParams, 50000);
	// HttpConnectionParams.setSoTimeout(httpParams, 50000);
	// HttpClient client = new DefaultHttpClient(httpParams);
	// HttpGet get = new HttpGet(url + buffer.toString());
	// GoOutDebug.i("disableToken Tencent", "REQUEST URL:" + url + buffer.toString());
	// HttpResponse response;
	// response = client.execute(get);
	// String result = null;
	// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// HttpEntity entity = response.getEntity();
	// Header header = entity.getContentEncoding();
	// if (header == null || !"gzip".equalsIgnoreCase(header.getValue())) {
	// result = ThirdHttpUtil.requestResult(entity.getContent(), false);
	// } else {
	// result = ThirdHttpUtil.requestResult(entity.getContent(), true);
	// }
	// }
	// GoOutDebug.e("disableToken Tencent", "result=" + result);
	// /**
	// * {"data":null,"errcode":0,"msg":"ok","ret":0,"seqid":5865098827071043046}
	// */
	// // 获得返回结果
	// if (result != null && result.length() > 0) {
	// JSONObject jsonObj = new JSONObject(result);
	//
	// boolean unOauthed = false;
	//
	// if (jsonObj.getString("msg").equals("ok") && jsonObj.getInt("errcode") == 0) {
	// unOauthed = true;
	// } else if (jsonObj.has("detailerrinfo")) {
	// unOauthed = true;
	// /**
	// * {"data":null, "detailerrinfo":
	// * {"accesstoken":"","apiname":"weibo.t.add","appkey":"801339872"
	// * ,"clientip":"119.57.161.42"
	// * ,"cmd":0,"proctime":0,"ret1":3,"ret2":3,"ret3":36,"ret4":545632522
	// * ,"timestamp":1365575362
	// * },"errcode":36,"msg":"check sign error","ret":3,"seqid":5865101520015480007}
	// */
	//
	// /**
	// * {"data":null, "detailerrinfo":
	// * {"accesstoken":"","apiname":"weibo.auth.revoke_auth","appkey"
	// * :"801339872","clientip":"119.57.161.42"
	// * ,"cmd":0,"proctime":47,"ret1":255,"ret2":4,"ret3"
	// * :111,"ret4":2770568458,"timestamp":1365575312
	// * },"errcode":111,"msg":"server error","ret":4,"seqid":5865101305275817547}
	// */
	//
	// }
	//
	// if (unOauthed) {
	// GoOutController.getInstance().deleteAccountToken(context, Account.TYPE_QQWEIBO);
	// return true;
	// }
	// }
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// return false;
	// }
	//
	// /**
	// * 刷新token(过期时可能会用到)
	// *
	// * @Description:
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-25
	// */
	// public static void refreshToken() {
	//
	// /**
	// * https://open.t.qq.com/cgi-bin/oauth2/access_token?client_id=APP_KEY&grant_type=refresh_token&
	// * refresh_token=REFRESH_TOKEN 请求参数 字段 必须 说明 client_id true 申请应用时分配的app_key grant_type true
	// * 固定为“refresh_token” refresh_token true 上次授权或者刷新时获取的refresh_token
	// */
	//
	// }

}
