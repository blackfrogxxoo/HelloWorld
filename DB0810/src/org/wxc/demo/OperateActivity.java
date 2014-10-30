package org.wxc.demo;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class OperateActivity extends Activity {
	// 声明布局控件对象
	private EditText sql = null;
	private Button execute = null;
	private Button display = null;
	private TextView result = null;
	private ListView membersList = null;
	private SimpleAdapter adapter = null;// 适配器
	private List<Map<String, Object>> members = null;
	private CharSequence sayOK = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table);
		// 实例化布局控件对象
		this.sql = (EditText) super.findViewById(R.id.sql);
		this.execute = (Button) super.findViewById(R.id.execute);
		this.display = (Button) super.findViewById(R.id.display);
		this.result = (TextView) super.findViewById(R.id.result);
		this.membersList = (ListView) super.findViewById(R.id.membersList);
		// 设置“执行”按钮监听事件
		execute.setOnClickListener(new ExecuteListener());
		display.setOnClickListener(new DisplayListener());
		membersList.setOnItemLongClickListener(new LongClickListener());
	}

	/*
	 * 为membersList创建长按事件处理方法
	 */
	public class LongClickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView parent, View view,
				int position, long id) {
			// Log.e("CallLogActivity", view.toString() + "position=" +
			// position);
			// CharSequence number = ((TextView) view).getText();
			Toast t = Toast.makeText(OperateActivity.this, "长按了这个条目",
					Toast.LENGTH_LONG);
			t.show();
			return true;
		}
	}

	// ============================================================================//
	/*
	 * 内部类一："执行"按钮的监听类 用于实例化client类 发送消息
	 */
	public class ExecuteListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// 停止输入sql语句，记录
			String executeStr = sql.getText().toString();
			// 实例化MyServer类，要新建线程：MyServer
			try {
				makeMyServer(executeStr);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * 进度条
			 */
			/*final ProgressDialog proDia =new  ProgressDialog(OperateActivity.this);
			proDia.setTitle("正在下载");
			proDia.setMessage("请耐心等待...");
			proDia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			proDia.setMax(100);
			proDia.setProgress(30);
			
			proDia.setButton("后台处理", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					proDia.dismiss();
				}
			});
			proDia.setButton2("详细信息", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			proDia.onStart();
			new Thread() {
				public void run() {
					for (int x = 0; x < 100; x++) {
						try {
							Thread.sleep(500);
						} catch (Exception e) {
							e.printStackTrace();
						}
						proDia.incrementProgressBy(1);
					}
					proDia.dismiss();
				}
			}.start();
			proDia.show();
*/
		}

	}

	// ============================================================================//
	/*
	 * 内部类二："显示"按钮的监听类 用于实例化server 最终要实现的是：显示错误提示；显示查询结果；显示更新结果
	 */
	public class DisplayListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String str = sql.getText().toString();
			String updateResult = null;
			System.out.println("DisplayListener:" + members);
			// 加入SQL语句判断,true则调用myDisplay(),false则调用myDisplay(String)
			if (isSelect(str)) {
				System.out.println(new MyServer().getMembers());
				myDisplay();
			} else
				myDisplay(updateResult);
		}

	}

	// ============================================================================//
	/*
	 * 方法一：实例化MyServer类，完成相关操作 makeMyServer()
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	private void makeMyServer(String str) throws InterruptedException {
		MyServer myServer = new MyServer(str);
		Thread serverThread = new Thread(myServer, "线程Server");
		serverThread.start();
		/*
		 * members = new MyServer().getMembers();
		 * System.out.println("makeMyServer: 接收到members:"+members);
		 */

	}

	// ============================================================================//
	/*
	 * 方法二：实例化MyClient类，完成相关操作 makeMyClient()
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	/*
	 * public void makeMyClient(String str) { MyClient myClient = new
	 * MyClient(str); Thread clientThread = new Thread(myClient, "线程Client");
	 * clientThread.start(); // 加入SQL语句判断 }
	 */
	// ============================================================================//
	/*
	 * 方法三：显示结果 重载一：显示查询结果
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	public void myDisplay() {

		System.out.println("myDisplay(): 尝试显示...");
		try {
			List<Map<String, Object>> members = new MyServer().getMembers();
			System.out.println(members);
			// List对象members的适配器
			membersList.setAdapter(new SimpleAdapter(this, members,
					R.layout.member, new String[] { "_id", "number", "date",
							"name" }, new int[] { R.id._id, R.id.number,
							R.id.date, R.id.name }));
			System.out.println("myDisplay(): 显示成功！");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("myDisplay(): 显示失败！");
		}

	}

	// ============================================================================//
	/*
	 * 方法三：显示结果 重载二：显示更新结果
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	private void myDisplay(String updateResult) {
		this.result.setText(this.sayOK);
	}

	// ============================================================================//
	/*
	 * 方法四：判断SQL语句是查询还是更新 boolen isQuery(sql)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	private boolean isSelect(String sql) {
		if (sql.startsWith("SELECT") || sql.startsWith("select")) {
			System.out.println("isSelect(): 提交的是查询操作！");
			return true;
		} else {
			System.out.println("isSelect(): 提交的是更新操作！");
			return false;
		}

	}

	// ============================================================================//
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
