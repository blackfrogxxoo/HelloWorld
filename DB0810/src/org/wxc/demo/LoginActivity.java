package org.wxc.demo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	//声明布局控件对象
	private EditText user = null ;
	private EditText password = null ;
	private Button login = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //实例化布局控件对象
        this.user=(EditText)super.findViewById(R.id.user);
        this.password=(EditText)super.findViewById(R.id.password);
        this.login=(Button)super.findViewById(R.id.login);
        //login监听事件处理
        login.setOnClickListener(new LoginListener());
    }
    /*	方法一：实例化LoginHelper对象
     * 点击“登录”时调用
     * private boolean rightLogin()
     * in-parm: EditText.getText()
     */
	 private boolean rightLogin(String userStr,String passwordStr){
		 //获取输入信息
		 LoginHelper loginHelper = new LoginHelper(userStr,passwordStr);
		 if(loginHelper.isLogin()){
			 return true;
		 }else return false;
	 }
//==========================================================================//	 
	 /*	内部类一：login按钮的监听类
	  * (non-Javadoc)
	  * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	  */
	 public class LoginListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			//停止输入信息，记录
			String userStr = user.getText().toString();
			String passwordStr = password.getText().toString();
			
			if(rightLogin(userStr,passwordStr)){
				try{
					myIntent();
				}catch(Exception e){
					System.out.println("无法跳转!");
				}
			}else{
				Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
			}
			
		}
		 
	 }
//==========================================================================//	 
	 /*	方法二：Intent到OperateActivity
	  * private void myIntent()
	  * 在监听事件中，经验证后调用
	  */
	 private void myIntent(){
		 Intent it = new Intent(LoginActivity.this, OperateActivity.class);
			LoginActivity.this.startActivityForResult(it, 1);
	 }
//==========================================================================//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

}
