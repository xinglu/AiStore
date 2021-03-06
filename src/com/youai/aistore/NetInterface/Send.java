package com.youai.aistore.NetInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youai.aistore.R;
import com.youai.aistore.Util;
import com.youai.aistore.Bean.Base;
import com.youai.aistore.Bean.CommentsBean;
import com.youai.aistore.Bean.CommitOrderBean;
import com.youai.aistore.Bean.ConsigneeBean;
import com.youai.aistore.Bean.GoodsBean;
import com.youai.aistore.Bean.ListCommentsBean;
import com.youai.aistore.Bean.ListFclassTwo;
import com.youai.aistore.Bean.ListGoodsBean;
import com.youai.aistore.Bean.ListOrderBean;
import com.youai.aistore.Bean.ListOrderBean.OrderBean;
import com.youai.aistore.Bean.ListShopCartBean;
import com.youai.aistore.Bean.OrderDetailsBean;
import com.youai.aistore.Bean.OrderDetailsBean.Goods;
import com.youai.aistore.Bean.ShopCartBean;
import com.youai.aistore.Bean.UserBean;

public class Send {
	private Context context;
	private Gson gson;

	public Send(Context context) {
		this.context = context;
		gson = new Gson();

	}

	/**
	 * 获取首页信息
	 */
	public ListGoodsBean RequestHome(String time) {
		ListGoodsBean List = new ListGoodsBean();
		String key = Util.hashKeyForDisk("AIAI.CN_" + time);
		String url = ServiceUrl.HomeUrl + key;
		String jsonStr = null;
		jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					List.setCode(200);
					List.setMsg(object.getString("message"));
					JSONObject data = object.getJSONObject("data");
					Type type = new TypeToken<ArrayList<GoodsBean>>() {
					}.getType();
					ArrayList<ArrayList<GoodsBean>> l = new ArrayList<ArrayList<GoodsBean>>();
					for (int i = 1; i < 9; i++) {
						String json = data.getString("ad_" + i).toString();
						ArrayList<GoodsBean> s = gson.fromJson(json, type);
						l.add(s);
					}
					List.setList(l);
					return List;
				} else {
					List.setMsg(object.getString("message"));
					List.setCode(object.getInt("code"));
					return List;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			List.setCode(500);
			List.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return List;
		}

		return null;

	}

	/**
	 * 获取单品信息
	 */
	@SuppressWarnings("null")
	public GoodsBean GetProductDetails(int id) {
		GoodsBean bean = null;
		String url = ServiceUrl.Product_Details_Url + id;
		String jsonStr = null;
		jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONObject data = object.getJSONObject("data");
					Type type = new TypeToken<GoodsBean>() {
					}.getType();
					String json = data.toString();
					bean = gson.fromJson(json, type);
					JSONArray array = data.getJSONArray("picurls");
					ArrayList<String> picurls = new ArrayList<String>();
					for (int i = 0; i < array.length(); i++) {
						picurls.add(array.getString(i));
					}
					bean.setPicurls(picurls);
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}

		return null;

	}

	/**
	 * 获取单品评论
	 */
	@SuppressWarnings("unused")
	public ListCommentsBean GetProductComments(int id, int page) {
		ListCommentsBean list = new ListCommentsBean();
		CommentsBean bean = null;
		String url = ServiceUrl.Product_comments_Url_head + id
				+ ServiceUrl.Product_comments_Url_foot + page;
		System.out.println("pinglunurl=================" + url);
		String jsonStr = null;
		jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONArray data = object.getJSONArray("data");
					String json = data.toString();
					Type type = new TypeToken<ArrayList<CommentsBean>>() {
					}.getType();
					ArrayList<CommentsBean> l = gson.fromJson(json, type);

					list.setList(l);
					list.setCode(200);
					list.setMsg(object.getString("message"));
					return list;
				} else {
					list.setMsg(object.getString("message"));
					list.setCode(object.getInt("code"));
					return list;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			list.setCode(500);
			list.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return list;
		}

		return null;

	}

	/**
	 * 获取一级分类
	 */
	public ListGoodsBean GetFclassFrist(int id) {
		ListGoodsBean list = new ListGoodsBean();
		ArrayList<ArrayList<GoodsBean>> listb = new ArrayList<ArrayList<GoodsBean>>();
		String url = ServiceUrl.Product_fclass_Url_frist + id;
		String jsonStr = null;
		jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONObject data = object.getJSONObject("data");
					JSONArray titlearray = data.getJSONArray("sub_categories");
					for (int i = 0; i < titlearray.length(); i++) {
						JSONObject j = titlearray.getJSONObject(i);
						String sub_id = j.getString("sub_cate_id");
						JSONObject soj = data.getJSONObject("category_2");
						JSONArray t = soj.getJSONArray(sub_id);
						String json = t.toString();
						Type type = new TypeToken<ArrayList<GoodsBean>>() {
						}.getType();
						ArrayList<GoodsBean> l = gson.fromJson(json, type);
						if (l != null) {
							listb.add(l);
						}
					}
					list.setList(listb);
					list.setCode(200);
					list.setMsg(object.getString("message"));
					return list;
				} else {
					list.setMsg(object.getString("message"));
					list.setCode(object.getInt("code"));
					return list;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			list.setCode(500);
			list.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return list;
		}

		return null;

	}

	/**
	 * 获取二级分类
	 */
	public ListFclassTwo GetFclassTwo(int id, String asctype, int page) {
		ListFclassTwo list = new ListFclassTwo();
		String url = ServiceUrl.Product_fclass_Url_two_head + id
				+ ServiceUrl.Product_fclass_Url_two_center + asctype
				+ ServiceUrl.Product_fclass_Url_two_foot + page;
		String jsonStr = null;
		jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONObject data = object.getJSONObject("data");
					JSONArray array = data.getJSONArray("sub_cate_content");
					String json = array.toString();
					Type type = new TypeToken<ArrayList<GoodsBean>>() {
					}.getType();
					ArrayList<GoodsBean> l = gson.fromJson(json, type);
					list.setList(l);
					list.setCode(200);
					list.setMsg(object.getString("message"));
					return list;
				} else {
					list.setMsg(object.getString("message"));
					list.setCode(object.getInt("code"));
					return list;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			list.setCode(500);
			list.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return list;
		}

		return null;

	}

	/**
	 * 加入购物车
	 */
	public Base AddShopCart(int good_id, int number, String session_id,
			String userid) {
		Base bean = new Base();
		String url = ServiceUrl.Product_AddShopCart_Url_head + good_id
				+ ServiceUrl.Product_AddShopCart_Url_center + number
				+ ServiceUrl.Product_AddShopCart_Url_foot_sessid + session_id
				+ ServiceUrl.Product_AddShopCart_Url_foot_userid + userid;
		String jsonStr = null;
		jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					System.out.println("result===============" + jsonStr);
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}

		return null;

	}

	/**
	 * 购物车列表
	 */
	public ListShopCartBean getShopCartlist(String sessionid, String userid) {
		ListShopCartBean list = new ListShopCartBean();
		String url = ServiceUrl.GetShopCartList_Url_head + sessionid
				+ ServiceUrl.GetShopCartList_Url_foot + userid;
		String jsonStr = null;
		jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					ArrayList<ShopCartBean> l = new ArrayList<ShopCartBean>();
					JSONObject data = object.getJSONObject("data");
					JSONArray datas = data.getJSONArray("datas");
					for (int i = 0; i < datas.length(); i++) {
						JSONObject j = datas.getJSONObject(i);
						String json = j.toString();
						Type type = new TypeToken<ShopCartBean>() {
						}.getType();
						ShopCartBean b = gson.fromJson(json, type);
						l.add(b);
					}
					String count_price = String.valueOf(data
							.getInt("count_price"));
					list.setList(l);
					list.setCode(200);
					list.setCount_price(count_price);
					list.setMsg(object.getString("message"));
					return list;
				} else {
					list.setMsg(object.getString("message"));
					list.setCode(object.getInt("code"));
					return list;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			list.setCode(500);
			list.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return list;
		}

		return null;

	}

	/**
	 * 从购物车删除
	 */
	public Base DeletefromShopCart(String res_id, String session_id,
			String userid) {
		Base bean = new Base();
		String url = ServiceUrl.Product_deletefromShopCart_Url_head + res_id
				+ ServiceUrl.Product_deletefromShopCart_Url_foot_sessid
				+ session_id
				+ ServiceUrl.Product_deletefromShopCart_Url_foot_userid
				+ userid;
		String jsonStr = null;
		jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}

		return null;

	}

	/**
	 * 登录
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public UserBean getLogin(String username, String password) {
		UserBean bean = new UserBean();
		String url = ServiceUrl.Login_Url_username + username
				+ ServiceUrl.Login_Url_password + password;
		String jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONObject data = object.getJSONObject("data");
					String json = data.toString();
					Type type = new TypeToken<UserBean>() {
					}.getType();
					bean = gson.fromJson(json, type);
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					System.out.println(object.get("code") + "登陆成功");// 测试是不是200
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}
		return null;
	}

	/**
	 * 注册
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public UserBean regist(String username, String password) {
		UserBean bean = new UserBean();
		String url = ServiceUrl.Regist_Url_username + username
				+ ServiceUrl.Regist_Url_password + password;
		String jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONObject data = object.getJSONObject("data");
					String json = data.toString();
					Type type = new TypeToken<UserBean>() {
					}.getType();
					bean = gson.fromJson(json, type);
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					System.out.println(object.get("code") + "注册成功");// 测试是不是200
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}
		return null;
	}

	/**
	 * 获取收货人信息
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public ConsigneeBean getConsigneeInfo(String userid) {
		ConsigneeBean bean = new ConsigneeBean();
		String url = ServiceUrl.get_consignee_info + userid;
		String jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			System.out.println("收件人信息"+jsonStr);
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONObject data = object.getJSONObject("data");
					String json = data.toString();
					Type type = new TypeToken<ConsigneeBean>() {
					}.getType();
					bean = gson.fromJson(json, type);
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}
		return null;
	}

	/**
	 * save收货人信息
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public Base saveConsigneeInfo(String userid, String consignee, String tel,
			String address) {
		Base bean = new Base();
		String url = ServiceUrl.save_consignee_info + userid
				+ ServiceUrl.save_consignee_info_consignee +java.net.URLEncoder.encode(consignee) 
				+ ServiceUrl.save_consignee_info_tel  +java.net.URLEncoder.encode(tel) 
				+ ServiceUrl.save_consignee_info_address +java.net.URLEncoder.encode(address) ;
		String jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}
		return null;
	}

	/**
	 * 提交订单
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public CommitOrderBean CommitOrder(String userid, String time, String type, String fee) {
		CommitOrderBean bean = new CommitOrderBean();
		String url = ServiceUrl.save_order_info + userid
				+ ServiceUrl.save_order_info_time + time
				+ ServiceUrl.save_order_info_type + type
				+ ServiceUrl.save_order_info_time_fee + fee;
		String jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONObject data = object.getJSONObject("data");
					bean.setOrder_sn(data.getString("order_sn"));
					
					
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}
		return null;
	}

	/**
	 * 获取全部订单
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public ListOrderBean getAllOrderlist(String userid, int page) {
		ListOrderBean bean = new ListOrderBean();
		String url = ServiceUrl.get_order_list_userid + userid
				+ ServiceUrl.get_order_list_page + page
				+ ServiceUrl.get_order_list_action;
		String jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONArray data = object.getJSONArray("data");
					String json = data.toString();
					Type t = new TypeToken<ArrayList<OrderBean>>() {
					}.getType();
					ArrayList<OrderBean> b = gson.fromJson(json, t);
					bean.setList(b);
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}
		return null;
	}

	/**
	 * 获取订单详情
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public OrderDetailsBean getOrderDetalis(String orderid, String userid) {
		OrderDetailsBean bean = new OrderDetailsBean();
		String url = ServiceUrl.get_order_details_orderid + orderid
				+ ServiceUrl.get_order_details_userid + userid;
		String jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					JSONObject data = object.getJSONObject("data");
					String json = data.toString();
					Type t = new TypeToken<OrderDetailsBean>() {
					}.getType();
					bean = gson.fromJson(json, t);
					JSONArray j = data.getJSONArray("goods");
					Type tg = new TypeToken<ArrayList<Goods>>() {
					}.getType();
					ArrayList<Goods> gs = gson.fromJson(j.toString(), tg);
					bean.setGoods(gs);
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}
		return null;
	}
	
	
	/**
	 * 同步购物车信息
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public Base updataShopcartInfo(String sessionid, String userid) {
		Base bean = new Base();
		String url = ServiceUrl.updata_shopcart_session_id + sessionid
				+ ServiceUrl.updata_shopcart_user_id + userid;
		String jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}
		return null;
	}
	
	
	/**
	 * 修改订单状态
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public Base UpdataOrderStatu(String order_sn) {
		Base bean = new Base();
		String url = ServiceUrl.updata_order_statu_order + order_sn;
				
		String jsonStr = GetHttp.sendGet(url);

		if (jsonStr != null && !jsonStr.equals("")) {
			JSONObject object = null;
			try {
				object = new JSONObject(jsonStr);
				if (object.get("code") != null && object.getInt("code") == 200) {
					bean.setCode(200);
					bean.setMsg(object.getString("message"));
					return bean;
				} else {
					bean.setMsg(object.getString("message"));
					bean.setCode(object.getInt("code"));
					return bean;

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			bean.setCode(500);
			bean.setMsg(context.getResources().getString(
					R.string.http_status_code_error));
			return bean;
		}
		return null;
	}

}
