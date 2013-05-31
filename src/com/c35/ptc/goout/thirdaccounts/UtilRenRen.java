package com.c35.ptc.goout.thirdaccounts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.interfaces.ThirdAccountTokenListener;

/**
 * 人人网部分api调用
 * 
 * 详见：http://wiki.dev.renren.com/wiki/API
 * 
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-9
 */
public class UtilRenRen {

	/**
	 * 获取人人Token
	 * 
	 * @Description:
	 * @param context
	 * @param code
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-9
	 */
	public static void getRenRenToken(Context context, String code, ThirdAccountTokenListener listener) {

		try {
			String urlPath = "https://graph.renren.com/oauth/token?";
			URL url = new URL(urlPath);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setDoOutput(true);
			OutputStream out = request.getOutputStream();
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code").append("&client_id=").append(ThirdConfigConstants.RENREN_API_KEY).append("&client_secret=").append(ThirdConfigConstants.RENREN_SECRET_KEY).append("&redirect_uri=").append(ThirdConfigConstants.RENREN_OAUTH_CALLBACK).append("&code=").append(code);
			out.write((sb.toString()).getBytes());
			GoOutDebug.e("getRenRenToken Renren", "url=" + urlPath + sb.toString());
			out.flush();
			request.connect();
			int responseCode = request.getResponseCode();
			if (200 == responseCode) {
				InputStream inputStream = request.getInputStream();
				String result = new String(ThirdHttpUtil.readStream(inputStream));
				GoOutDebug.e("getRenRenToken Renren", "result=" + result);
				JSONObject object = new JSONObject(result);
				String openExpire = object.getString("expires_in");
				String refreshToken = object.getString("refresh_token");
				String accessToken = object.getString("access_token");

				Account account = new Account();
				account.setExpiresIn(openExpire);
				account.setRefreshToken(refreshToken);
				account.setAccessToken(accessToken);

				JSONObject userInfo = object.getJSONObject("user");
				account.setThirdId(userInfo.getInt("id") + "");
				account.setAccountName(userInfo.getString("name"));

				JSONArray arrayAvatar = userInfo.getJSONArray("avatar");
				for (int i = 0; i < arrayAvatar.length(); i++) {
					JSONObject obj = (JSONObject) arrayAvatar.get(i);
					if ((obj.getString("type")).equals("avatar")) {
						account.setLogo(obj.getString("url"));
					}
				}
				account.setAccountType(Account.TYPE_RENREN);
				listener.getTokenSuccess(account);
				return;

				/**
				 * {
				 * 
				 * "scope":"status_update publish_share",
				 * 
				 * "expires_in":2595139,
				 * 
				 * "refresh_token":"231482|0.9hF5DtTIvNLOb43VSP5uwIzhvFsmRTW9.269295649.1365497828736",
				 * 
				 * "user":{
				 * 
				 * "id":269295649,"name":"庄广钰",
				 * 
				 * "avatar":[
				 * 
				 * {"type":"avatar","url":
				 * "http://hdn.xnimg.cn/photos/hdn221/20121104/2230/h_head_MYgG_5db6000032181376.jpg"},
				 * 【100x100】
				 * 
				 * {"type":"tiny","url":
				 * "http://hdn.xnimg.cn/photos/hdn221/20121104/2230/h_tiny_sxbP_5db6000032181376.jpg"},【50x50】
				 * 
				 * {"type":"main","url":
				 * "http://hdn.xnimg.cn/photos/hdn221/20121104/2230/h_main_rp8k_5db6000032181376.jpg"
				 * },【200x200】
				 * 
				 * {"type":"large","url":
				 * "http://hdn.xnimg.cn/photos/hdn221/20121104/2230/h_large_iVfD_5db6000032181376.jpg"}【更大】
				 * 
				 * ] },
				 * 
				 * "access_token":"231482|6.7262338f1b4b448d34f9d35cb8229d58.2592000.1368774000-269295649"
				 * 
				 * }
				 */

				/**
				 * // * uid int 表示用户id // * // * name string 表示用户名 // * // * headurl string 表示头像链接 100*100大小
				 * //
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		listener.getTokenFail();
	}

	/**
	 * 发布一条动态
	 * 
	 * 详见 ：http://wiki.dev.renren.com/wiki/Status.set
	 * 
	 * @Description:
	 * @param token
	 * @param content
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-9
	 */
	public static boolean updateOneStatues(Context con, String token, String content) {

		try {
			String url = "https://api.renren.com/restserver.do";
			GoOutDebug.e("updateOneStatues Renren", "request url=" + url);
			HttpClient request = ThirdHttpUtil.getNewHttpClient();
			HttpPost httpPost = new HttpPost(new URI(url));
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("method", "status.set"));
			params.add(new BasicNameValuePair("v", "1.0"));// API的版本号，固定值为1.0
			params.add(new BasicNameValuePair("access_token", token));// OAuth2.0验证授权后获得的token。同时兼容session_key方式调用
			params.add(new BasicNameValuePair("format", "JSON"));// 返回值的格式。请指定为JSON或者XML，推荐使用JSON，缺省值为XML
			params.add(new BasicNameValuePair("status", content));// 用户更新的状态信息，最多140个字符
			// params.add(new BasicNameValuePair("place_id", ));//
			// 发状态时所在地点的ID。place_id为一个地点的Id，可以通过places.create来创建地点，生成place_id。

			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			HttpResponse resposne = request.execute(httpPost);
			if (200 == resposne.getStatusLine().getStatusCode()) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ThirdHttpUtil.readStream(inputStream));

				GoOutDebug.e("updateOneStatues Renren", "result=" + result);

				JSONObject jsonObj = new JSONObject(result);
				if (jsonObj.has("result")) {
					int resultCode = jsonObj.getInt("result");

					if (resultCode == 1) {
						return true;
					} else if (resultCode == 2002) {
						// access token 过期 详见：http://wiki.dev.renren.com/wiki/API_Error_Code
						GoOutController.getInstance().deleteAccountToken(con, Account.TYPE_RENREN);
					}
				} else if (jsonObj.has("error_code")) {
					GoOutDebug.e("updateOneStatues Renren", "ERROR!");
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
		return false;
	}

	/******* 以下方法暂时未用 ********/
	// /**
	// * 分享(暂时不能用，会出现这种状态：自己分享了一条自己发的状态。)
	// *
	// * 详见： http://wiki.dev.renren.com/wiki/Share.share
	// *
	// * @Description:
	// * @return
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-9
	// */
	//
	// public static boolean shareOne(String token, String content) {
	//
	// try {
	// String url = "https://api.renren.com/restserver.do";
	// GoOutDebug.e("shareOne Renren", "request url=" + url);
	// HttpClient request = ThirdHttpUtil.getNewHttpClient();
	// HttpPost httpPost = new HttpPost(new URI(url));
	// ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("method", "share.share"));
	// params.add(new BasicNameValuePair("access_token", token));// OAuth2.0验证授权后获得的token。同时兼容session_key方式调用
	// params.add(new BasicNameValuePair("type", "6"));// 分享类型:日志为1、照片为2、链接为6、相册为8、视频为10、音频为11、分享为20
	// params.add(new BasicNameValuePair("v", "1.0"));// API的版本号，固定值为1.0
	// params.add(new BasicNameValuePair("url", "http://www.nengchuqu.com"));//
	// 人人网站外内容时，url为必须参数。此时type只能是：链接为6、视频为10、音频为11。
	// params.add(new BasicNameValuePair("comment", content));// 分享内容时，用户的评论内容
	//
	// // params.add(new BasicNameValuePair("source_link",
	// // "{\"text\":\"能留学\",\"href\":\"http://www.nengchuqu.com\"}"));// 来自应用
	// params.add(new BasicNameValuePair("format", "JSON"));// 返回值的格式。请指定为JSON或者XML，推荐使用JSON，缺省值为XML
	// // params.add(new BasicNameValuePair("place_id", ));//
	// // 发状态时所在地点的ID。place_id为一个地点的Id，可以通过places.create来创建地点，生成place_id。
	//
	// httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	//
	// HttpResponse resposne = request.execute(httpPost);
	// if (200 == resposne.getStatusLine().getStatusCode()) {
	// InputStream inputStream = resposne.getEntity().getContent();
	// String result = new String(ThirdHttpUtil.readStream(inputStream));
	//
	// GoOutDebug.e("shareOne Renren", "result=" + result);
	// JSONObject jsonObj = new JSONObject(result);
	// // 成功返回包含刚提交的字段
	// if (jsonObj.has("summary")) {
	// return true;
	// }
	// /**
	// * {"summary":"","comment_count":"0","share_count":0,"my_like":0,"type":6,"url":
	// * "http://www.nengchuqu.com"
	// * ,"id":15684018855,"resource_id":0,"title":"http://www.nengchuqu.com"
	// * ,"thumbnail_url":"","like_count"
	// * :0,"resource_owner_id":0,"user_id":269295649,"original_user_id":0}
	// */
	//
	// }
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	// /**
	// * 获取token后，立即获取用户信息，然后存储
	// *
	// * 详见： http://wiki.dev.renren.com/wiki/Users.getInfo
	// *
	// * @Description:
	// * @param account
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-17
	// */
	// private static boolean getAccountInfoAfterGetToken(Context con, Account account) {
	//
	// try {
	// String url = "https://api.renren.com/restserver.do";
	// GoOutDebug.e("updateOneStatues Renren", "request url=" + url);
	// HttpClient request = ThirdHttpUtil.getNewHttpClient();
	// HttpPost httpPost = new HttpPost(new URI(url));
	// ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("method", "users.getInfo"));
	// params.add(new BasicNameValuePair("v", "1.0"));// API的版本号，固定值为1.0
	// params.add(new BasicNameValuePair("access_token", account.getAccessToken()));//
	// OAuth2.0验证授权后获得的token。同时兼容session_key方式调用
	// params.add(new BasicNameValuePair("format", "JSON"));// 返回值的格式。请指定为JSON或者XML，推荐使用JSON，缺省值为XML
	// // params.add(new BasicNameValuePair("place_id", ));//
	// // 发状态时所在地点的ID。place_id为一个地点的Id，可以通过places.create来创建地点，生成place_id。
	//
	// httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	//
	// HttpResponse resposne = request.execute(httpPost);
	// if (200 == resposne.getStatusLine().getStatusCode()) {
	// InputStream inputStream = resposne.getEntity().getContent();
	// String result = new String(ThirdHttpUtil.readStream(inputStream));
	//
	// GoOutDebug.e("getAccountInfoAfterGetToken Renren", "result=" + result);
	//
	// JSONObject jsonObj = new JSONObject(result);
	//
	// /**
	// * uid int 表示用户id
	// *
	// * name string 表示用户名
	// *
	// * headurl string 表示头像链接 100*100大小
	// */
	//
	// return true;
	// }
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	// /**
	// * 判断token是否失效
	// *
	// * 详见：http://wiki.dev.renren.com/wiki/Users.isAppUser
	// *
	// * @Description:
	// * @param token
	// * @return
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-9
	// */
	// public static boolean isTokenEnabled(String token) {
	// try {
	// String url = "https://api.renren.com/restserver.do";
	// GoOutDebug.e("isTokenEnabled Renren", "request url=" + url);
	// HttpClient request = ThirdHttpUtil.getNewHttpClient();
	// HttpPost httpPost = new HttpPost(new URI(url));
	// ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("method", "users.isAppUser"));
	// params.add(new BasicNameValuePair("v", "1.0"));// API的版本号，固定值为1.0
	// params.add(new BasicNameValuePair("access_token", token));// OAuth2.0验证授权后获得的token。同时兼容session_key方式调用
	// params.add(new BasicNameValuePair("format", "JSON"));// 返回值的格式。请指定为JSON或者XML，推荐使用JSON，缺省值为XML
	// httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	//
	// HttpResponse resposne = request.execute(httpPost);
	// GoOutDebug.e("isTokenEnabled Renren", "resposne=" + resposne);
	// if (200 == resposne.getStatusLine().getStatusCode()) {
	// InputStream inputStream = resposne.getEntity().getContent();
	// String result = new String(ThirdHttpUtil.readStream(inputStream));
	//
	// GoOutDebug.e("isTokenEnabled Renren", "result=" + result);
	//
	// JSONObject jsonObj = new JSONObject(result);
	// int resultCode = jsonObj.getInt("result");
	//
	// if (resultCode == 1) {
	// return true;
	// }
	// }
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	// /**
	// * 取消人人授权
	// *
	// * @Description:
	// * @param context
	// * @return
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-9
	// */
	// public static boolean disableToken(Context context) {
	// return GoOutController.getInstance().deleteAccountToken(context, Account.TYPE_RENREN);
	// }
}
