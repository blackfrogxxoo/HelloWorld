package org.wxc.demo;

//使用时：实例化LoginHelper对象，然后调用isLogin方法用于判定
public class LoginHelper {
	/*	域
	 * user,password
	 */
	private String user = null ;
	private String password = null ;
	/*	构造方法
	 * LoginHelper(user,password)
	 * */
	public LoginHelper(String user,String password){
		this.user = user ;
		this.password = password;
	}
	/*   方法一：验证用户名名和密码
	  * private
	  * isLogin()
	  * 返回类型：boolean
	  * 形参：String user,String password
	  * 
	  */
   public boolean isLogin(){
	   if(user.equals("root")&&password.equals("wxc147258"))
		   return true;
	   else
   		return false;
   }
   /* 方法二：获取服务器返回的用户名和密码
    * private getUserAndPassword()
    * Client实例化，调用getUser(),getPassword()方法*/
   
}
