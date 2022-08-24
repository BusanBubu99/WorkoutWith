package com.bubu.interfacetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //This is ExampleCode
        CoroutineScope(Dispatchers.Default).launch {
            var t = UserSearchUnit("dong",3)
            var res = t.getApiData()
            //TODO(if(res == null))
            //TODO(if(res.responseCode != ))
            if(res == null) { //Exception
                Log.d("TestLog1", "Connection Error or Exception")
            } else { //Successful
                //TODO(if(res.get(0).responseCode == 0){)
                //
                //}
                Log.d("TestLog2", res.toString())
            }
        }
    }
}
