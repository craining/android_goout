package com.c35.ptc.goout.thirdaccounts;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.interfaces.ThirdAccountTokenListener;
import com.c35.ptc.goout.util.StringUtil;

/**
 * 新浪微博API调用
 * 
 * 详见：http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-9
 */
public class UtilSina {

	/**
	 * 获取新浪用户授权的token
	 * 
	 * @Description:
	 * @param context
	 * @param code
	 * @param handle
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-10
	 */
	public static void getSinaToken(final Context context, final String code, final ThirdAccountTokenListener listener) {
		String access_url = "https://api.weibo.com/oauth2/access_token";
		HttpClient request = ThirdHttpUtil.getNewHttpClient();
		HttpPost httpPost;
		try {
			httpPost = new HttpPost(new URI(access_url));
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("client_id", ThirdConfigConstants.SINA_APP_KEY));
			params.add(new BasicNameValuePair("client_secret", ThirdConfigConstants.SINA_APP_SECRET));
			params.add(new BasicNameValuePair("grant_type", "authorization_code"));
			params.add(new BasicNameValuePair("redirect_uri", ThirdConfigConstants.SINA_OAUTH_CALLBACK));
			params.add(new BasicNameValuePair("code", code));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			HttpResponse resposne = request.execute(httpPost);
			if (200 == resposne.getStatusLine().getStatusCode()) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ThirdHttpUtil.readStream(inputStream));

				GoOutDebug.e("getSinaToken", "result=" + result);

				/**
				 * {"access_token":"2.00SXcljBlx6PhC93f15b919339PGcD",
				 * 
				 * "remind_in":"157679999","expires_in":157679999,
				 * 
				 * "uid":"1592417504"}
				 */
				JSONObject obj = new JSONObject(result);
				String accessToken = obj.getString("access_token");
				String expiresIn = obj.getString("expires_in");
				String thirdId = obj.getString("uid");

				Account account = new Account();
				account.setThirdId(thirdId);
				account.setAccessToken(accessToken);
				account.setExpiresIn(expiresIn);

				account.setAccountType(Account.TYPE_SINA);

				getMoreUserInfo(account);

				// 昵称为空，id 为空
				if (!StringUtil.isNull(account.getAccountName()) && !StringUtil.isNull(account.getThirdId())) {
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
	public static void getMoreUserInfo(Account account) {

		try {
			HttpResponse resposne;
			InputStream inputStream;
			String result;
			String url = "https://api.weibo.com/2/users/show.json?access_token=" + account.getAccessToken() + "&uid=" + account.getThirdId();
			GoOutDebug.e("getUserInfo Sina", "request url=" + url);
			HttpGet httpGet = new HttpGet(new URI(url));
			HttpClient request = ThirdHttpUtil.getNewHttpClient();
			resposne = request.execute(httpGet);
			GoOutDebug.e("getUserInfo Sina", "resposne=" + resposne.toString());
			if (200 == resposne.getStatusLine().getStatusCode()) {
				inputStream = resposne.getEntity().getContent();
				result = new String(ThirdHttpUtil.readStream(inputStream));

				GoOutDebug.e("getUserInfo Sina", "result=" + result);

				JSONObject object = new JSONObject(result);
				account.setAccountName(object.getString("screen_name"));
				account.setLogo(object.getString("profile_image_url"));
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
	 * 更新一条微博
	 * 
	 * @Description:
	 * @param accessToken
	 * @param content
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws Exception
	 * @throws JSONException
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-9
	 */
//	public static boolean updateOneStatues(Context con, String accessToken, String content) {
//
//		try {
//			String url = "https://api.weibo.com/2/statuses/update.json";
//			GoOutDebug.e("updateOneStatues Sina", "request url=" + url);
//			HttpClient request = ThirdHttpUtil.getNewHttpClient();
//			HttpPost httpPost = new HttpPost(new URI(url));
//			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//			params.add(new BasicNameValuePair("status", content));
//			params.add(new BasicNameValuePair("access_token", accessToken));
//			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//
//			HttpResponse resposne = request.execute(httpPost);
//			if (200 == resposne.getStatusLine().getStatusCode()) {
//				InputStream inputStream = resposne.getEntity().getContent();
//				String result = new String(ThirdHttpUtil.readStream(inputStream));
//				GoOutDebug.e("updateOneStatues Sina", "result=" + result);
//
//				JSONObject obj = new JSONObject(result);
//				if (obj.has("created_at")) {
//					// 含此字段，说明成功了
//					return true;
//				} else if (obj.has("error_code")) {
//					/**
//					 * 失败的返回格式 { "request" : "/statuses/home_timeline.json", "error_code" : "20502", "error" :
//					 * "Need you follow uid." }
//					 */
//					if (obj.getString("error_code").equals("21327")) {
//						// token 过期 详见：http://open.weibo.com/wiki/Error_code
//						GoOutController.getInstance().deleteAccountToken(con, Account.TYPE_SINA);
//					}
//				}
//			}
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return false;
//	}
	
	/**
	 * 更新一条微博
	 * 
	 * @Description:
	 * @param accessToken
	 * @param content
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws Exception
	 * @throws JSONException
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-9
	 */
	public static boolean aaaa(Context con, String accessToken, String uid) {

		try {
			String url = "https://api.weibo.com/2/friendships/create.json";
			HttpClient request = ThirdHttpUtil.getNewHttpClient();
			HttpPost httpPost = new HttpPost(new URI(url));
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//			params.add(new BasicNameValuePair("status", content));
//			params.add(new BasicNameValuePair("screen_name", "微博开放平台"));
			params.add(new BasicNameValuePair("access_token", accessToken));
			params.add(new BasicNameValuePair("uid", uid));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			HttpResponse resposne = request.execute(httpPost);
			int responseCode =  resposne.getStatusLine().getStatusCode();
			GoOutDebug.e("关注 Sina", "result=" + responseCode);
			
			if (200 == responseCode) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ThirdHttpUtil.readStream(inputStream));
				GoOutDebug.e("关注 Sina", "result=" + result);

//				JSONObject obj = new JSONObject(result);
//				if (obj.has("created_at")) {
//					// 含此字段，说明成功了
//					return true;
//				} else if (obj.has("error_code")) {
//					/**
//					 * 失败的返回格式 { "request" : "/statuses/home_timeline.json", "error_code" : "20502", "error" :
//					 * "Need you follow uid." }
//					 */
//					if (obj.getString("error_code").equals("21327")) {
//						// token 过期 详见：http://open.weibo.com/wiki/Error_code
//						GoOutController.getInstance().deleteAccountToken(con, Account.TYPE_SINA);
//					}
//				}
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
	

	/****** 以下方法暂时未用 *****/

	// /**
	// * 取消授权
	// *
	// * @Description:
	// * @param token
	// * @return true取消成功，false取消失败
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-9
	// */
	// public static boolean disableToken(String token) {
	//
	// try {
	// HttpResponse resposne;
	// InputStream inputStream;
	// String result;
	// String url = "https://api.weibo.com/oauth2/revokeoauth2?access_token=" + token;
	// GoOutDebug.e("disableToken Sina", "request url=" + url);
	// HttpGet httpGet = new HttpGet(new URI(url));
	// HttpClient request = ThirdHttpUtil.getNewHttpClient();
	// resposne = request.execute(httpGet);
	// if (200 == resposne.getStatusLine().getStatusCode()) {
	// inputStream = resposne.getEntity().getContent();
	// result = new String(ThirdHttpUtil.readStream(inputStream));
	// GoOutDebug.e("disableToken Sina", "result=" + result);
	//
	// JSONObject jsonObj = new JSONObject(result);
	// if (jsonObj.getString("result").equals("true")) {
	// return true;
	// }
	//
	// /**
	// * { "result":"true" }
	// *
	// */
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
	//
	// return false;
	//
	// }
	//
	// /**
	// * 判断token是否可用
	// *
	// * @Description:
	// * @param token
	// * @return true可用，false过期
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-9
	// */
	// public static boolean isTokenEnabled(String token) {
	//
	// try {
	// String url = "https://api.weibo.com/oauth2/get_token_info";
	// GoOutDebug.e("isTokenEnabled Sina", "request url=" + url);
	// HttpClient request = ThirdHttpUtil.getNewHttpClient();
	// HttpPost httpPost = new HttpPost(new URI(url));
	// ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("access_token", token));
	// httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	//
	// HttpResponse resposne = request.execute(httpPost);
	// if (200 == resposne.getStatusLine().getStatusCode()) {
	// InputStream inputStream = resposne.getEntity().getContent();
	// String result = new String(ThirdHttpUtil.readStream(inputStream));
	//
	// GoOutDebug.e("isTokenEnabled Sina", "result=" + result);
	//
	// JSONObject jsonObj = new JSONObject(result);
	// long leftTime = jsonObj.getLong("expire_in");
	//
	// if (leftTime > 60) {
	// return true;
	// }
	//
	// /**
	// * uid string 授权用户的uid。
	// *
	// * appkey string access_token所属的应用appkey。
	// *
	// * scope string 用户授权的scope权限。
	// *
	// * create_at string access_token的创建时间，从1970年到创建时间的秒数。
	// *
	// * expire_in string access_token的剩余时间，单位是秒数。
	// *
	// */
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
	//
	// return false;
	// }
	//
	//
}
