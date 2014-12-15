package uk.ing.websocket;



import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.java_websocket.WebSocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.format.Formatter;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;

import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;


public class MainActivity extends ActionBarActivity {
	
	//TAB_CLIENT
    TabHost tabHost;
    int count=0;
    boolean connect=false;
    boolean listen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText edit=(EditText)findViewById(R.id.editText1);
        edit.setVisibility(View.GONE);
        Button bt=(Button)findViewById(R.id.button4);
        bt.setVisibility(View.GONE);
        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {      
        	new Thread(new Task()).start();
            settings.edit().putBoolean("my_first_time", false).commit(); 
        }
     
        
        
        pre_layout();
    }
    public void ciao(View v){
    	
    	EditText text=(EditText)findViewById(R.id.edit1);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {      
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    class Task implements Runnable {
		@Override
		public void run() {
			 HttpClient Client = new DefaultHttpClient();
	            try
	            {  HttpGet httpget = new HttpGet("http://rokity.altervista.org/butta.php");
	                         ResponseHandler<String> responseHandler = new BasicResponseHandler();
	                         String SetServerString = Client.execute(httpget, responseHandler);	                      
	             }
	           catch(Exception ex)
	              {	                     Log.d("FAIL, description:",ex.toString());	               }
		}

	}
    
    
   
    public void pre_layout(){
 	   tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabSpec spec1=tabHost.newTabSpec("TAB 1");
        spec1.setContent(R.id.tab_cronaca);
        spec1.setIndicator("Client");
        TabSpec spec2=tabHost.newTabSpec("TAB 2");       
        spec2.setContent(R.id.tab_esteri);
        spec2.setIndicator("Server");        
        TabSpec spec3=tabHost.newTabSpec("TAB 3");
        spec3.setContent(R.id.tab_politica);
        spec3.setIndicator("Informations");        
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);       
        tabHost.addTab(spec3);    
        TextView tv=(TextView)findViewById(R.id.text1);
        tv.setVisibility(View.INVISIBLE);
        
        TextView tv1=(TextView)findViewById(R.id.ip);
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        tv1.setText(ip);
        
        TextView tv3=(TextView)findViewById(R.id.textView3);
        tv3.setText(Html.fromHtml(getString(R.string.mail1)));
        Linkify.addLinks(tv3, Linkify.ALL);
        tv3.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv4=(TextView)findViewById(R.id.textView4);
        tv4.setText(Html.fromHtml(getString(R.string.mail2)));
        Linkify.addLinks(tv4, Linkify.ALL);
        tv4.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv5=(TextView)findViewById(R.id.textView5);
        tv5.setText(Html.fromHtml(getString(R.string.web_page)));
        Linkify.addLinks(tv5, Linkify.ALL);
        tv5.setMovementMethod(LinkMovementMethod.getInstance());
        
        }
    
    public boolean controlla_dati(String text)
    {
    	if(text.length()==0){    		
    		 Toast.makeText(this, "HOST VALUE IS EMPTY", Toast.LENGTH_SHORT).show();
     		Intent intent=new Intent(this,MainActivity.class);
     		startActivity(intent);
     		return false;    		
    	}
    	WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
    	if (!wifi.isWifiEnabled()){
    	Toast.makeText(this, "WIFI ISN'T ENABLED", Toast.LENGTH_SHORT).show();
    	Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
    	return false;
    	}
    	return true;
    }
    
    public void connect(View v){
    
    if(connect==false){
    	EditText edit=(EditText)findViewById(R.id.edit1);
    	boolean val=controlla_dati(edit.getText().toString());
    	if(val==true){
    	connect=true;
    	Button bt=(Button)findViewById(R.id.button3);
    	bt.setText("Connect...");    	
    	new Thread(new Task2(edit.getText().toString())).start();
    	}
    	
    }
    else
    {
    	
    	 Button bt=(Button)findViewById(R.id.button3);
	       bt.setText("Connect");
	       EditText edit1=(EditText)findViewById(R.id.edit1);
	       edit1.setText("");
	       EditText edit=(EditText)findViewById(R.id.editText1);
	        edit.setVisibility(View.INVISIBLE);
	        Button bt1=(Button)findViewById(R.id.button4);
	        bt1.setVisibility(View.INVISIBLE);
    	connect=false;
    	e.close();
    }
    	
    }
    AutobahnClientTest e;
    class Task2 implements Runnable {
    	String link;
    	public  Task2(String s){
    		this.link=s;
    	}
		@Override
		public void run()  throws NullPointerException{
			
			
			URI uri = null;
			try {
				uri = new URI(link);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				
			}
			
			 Draft d = new Draft_17();
	    	 e=new AutobahnClientTest(d,uri,MainActivity.this);
	    	 
	    		
	    		 Thread t=new Thread(e);
	    		 t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

	    		        public void uncaughtException(Thread t, Throwable e) {
	    		        	MainActivity.this.runOnUiThread(new Runnable() {
	    		    		    public void run() {
	    		    		    	connect=false;
	    		    		    	EditText edit1=(EditText)findViewById(R.id.edit1);
	    		    			       edit1.setText("");
	    		    		    	Button bt=(Button)findViewById(R.id.button3);
	    		    			       bt.setText("Connect");
	    		    		        Toast.makeText(MainActivity.this, "MAYBE THE ADDRESS IS WRONG!! \n                     CHECK IT", Toast.LENGTH_LONG).show();
	    		    		    }
	    		    		});
	    		        }
	    		     });
	 			t.start();
	    		 
	    		
	    	
	    	
	    	
			
		}

	}
    public class AutobahnClientTest extends WebSocketClient  {

    	Context context;
    	public AutobahnClientTest( Draft d , URI uri,Context c) {
    		super( uri, d );
    		this.context=c;
    		
    	}
    	
    	@Override
    	public void onMessage( String message ) {
    		send( message );
    	}

    	@Override
    	public void onMessage( ByteBuffer blob ) {
    		getConnection().send( blob );
    	}

    	@Override
    	public void onError( Exception ex ) {
    		
    		
    		toast("ERROR!");   	}

    	@Override
    	public void onOpen( ServerHandshake handshake ) {
    		
    		connect=true;
    		
    		toast("CONNECTION OPENED");
    		connected();
    		
    	}

    	@Override
    	public void onClose( int code, String reason, boolean remote ) {toast("CONNECTION CLOSED");	    	}
    	
    	public void toast(final String msg){
    		MainActivity.this.runOnUiThread(new Runnable() {
    		    public void run() {
    		        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    		    }
    		});
    	} }
    
    	public void connected()
    	{MainActivity.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	
		       Button bt=(Button)findViewById(R.id.button3);
		       bt.setText("Close Connection");
		       EditText edit=(EditText)findViewById(R.id.editText1);
		        edit.setVisibility(View.VISIBLE);
		        Button bt1=(Button)findViewById(R.id.button4);
		        bt1.setVisibility(View.VISIBLE);
		    	
		    	
		    }
		});
    		
    	}
    	public void send(View v){
    		EditText edit=(EditText)findViewById(R.id.editText1);
    		String multiLines = edit.getText().toString();
    		String[] streets;
    		String delimiter = "\n";
    		streets = multiLines.split(delimiter);
    		e.onMessage(streets[streets.length-1]);
    		
    	}

    	
    	
    	
    	//TAB_SERVER
    	
    	public void click(View v){
    		EditText text=(EditText)findViewById(R.id.edit2);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);
    	}
    	
    	
    	public void wlisten(View v){
    		//controllo se ha scritto qualcosa
    		//controllo se c'è connessione
    		if(listen==false){
    			EditText edit=(EditText)findViewById(R.id.edit2);
    			String text=edit.getText().toString();
    			boolean check=controlla_dati(text);
    			if(check==true){
    			listen=true;
    		final Button bt=(Button)findViewById(R.id.button5);
        	bt.setText("Listen...");
        	Thread t;
        t=	new Thread(new Task3(edit.getText().toString()));
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

	        public void uncaughtException(Thread t, Throwable e) {
	        	MainActivity.this.runOnUiThread(new Runnable() {
	    		    public void run() {
	    		    	listen=false;
	    		    	bt.setText("Listen");
	    		    	EditText edit=(EditText)findViewById(R.id.edit2);
	    		    	edit.setText("");
	    		        Toast.makeText(MainActivity.this, "MAYBE THE PORT IS WRONG!! \n                     CHECK IT", Toast.LENGTH_LONG).show();}});}});
        t.start();}}
    		else
    		{	listen=false;    			
    			Button bt=(Button)findViewById(R.id.button5);
    			bt.setText("Listen");
    			EditText edit=(EditText)findViewById(R.id.edit2);
    			edit.setText("");
    			TextView tv=(TextView)findViewById(R.id.text1);
    			tv.setText("");
    			tv.setVisibility(View.INVISIBLE);}}
    	
    	
    	
    	AutobahnServerTest server;
    	 class Task3 implements Runnable {
    	    	String link;
    	    	public  Task3(String s){
    	    		this.link=s;
    	    	}
    			@Override
    			public void run() {
    			
    				
    		    	Draft d = new Draft_17();
    		    	 try {
						server=new AutobahnServerTest(Integer.valueOf(link),d,MainActivity.this);
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    		    Thread t=new Thread(server);
    		   
    		    t.start();
    			}

    		}
    	
    	
    	 public class AutobahnServerTest extends WebSocketServer {
    			 int counter = 0;
    			Context context;
    			public AutobahnServerTest( int port , Draft d ,Context c ) throws UnknownHostException {
    				super( new InetSocketAddress( port ), Collections.singletonList( d ) );
    				this.context=c;
    				
    			}
    			
    			public AutobahnServerTest( InetSocketAddress address, Draft d ) {
    				super( address, Collections.singletonList( d ) );
    			}

    			
    			public void onOpen( WebSocket conn, ClientHandshake handshake ) {
    				counter++;
    				
    				toast("CONNECTION OPENED!");
    				connected();
    			}


    			public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
    				
    				toast("CONNECTION CLOSE!");
    			}

    			
    			public void onError( WebSocket conn, Exception ex ) {
    				
    				ex.printStackTrace();
    			}

    			
    			public void onMessage( WebSocket conn, String message ) {
    				
    				add_text(message);
    				
    			}

    			
    			public void onMessage( WebSocket conn, ByteBuffer blob ) {
    				conn.send( blob );
    			}

    			
    			public void onWebsocketMessageFragment( WebSocket conn, Framedata frame ) {
    				FrameBuilder builder = (FrameBuilder) frame;
    				builder.setTransferemasked( false );
    				conn.sendFrame( frame );
    			}

    			public void toast(final String msg){
    	    		MainActivity.this.runOnUiThread(new Runnable() {
    	    		    public void run() {
    	    		        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    	    		    }
    	    		});
    	    	}
    			public void connected()
    	    	{MainActivity.this.runOnUiThread(new Runnable() {
    			    public void run() {
    			    	
    			       Button bt=(Button)findViewById(R.id.button5);
    			       bt.setText("Close Connection");
    			      TextView tv=(TextView)findViewById(R.id.text1);
    			      tv.setVisibility(View.VISIBLE);
    			    	
    			    	
    			    }
    			});
    	    	}
    			
    			public void add_text(final String msg){
    				MainActivity.this.runOnUiThread(new Runnable() {
        			    public void run() {
        			    	
        			       

        			      TextView tv=(TextView)findViewById(R.id.text1);
        			     String text=tv.getText().toString();
        			     text=text+msg+"\n";
        			     tv.setText(text);

        			    	
        			    	
        			    }
        			});
    				
    			}

    		}

    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    
}
