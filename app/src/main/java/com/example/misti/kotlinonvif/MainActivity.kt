package com.example.misti.kotlinonvif

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.pedro.vlc.VlcListener
import com.pedro.vlc.VlcVideoLibrary
import com.rvirin.onvif.onvifcamera.*

class MainActivity : AppCompatActivity(), OnvifListener ,VlcListener {

    var vlcVideoLibrary: VlcVideoLibrary? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ipporttext=findViewById<EditText>(R.id.editText_ip)
val btn_connect=findViewById<Button>(R.id.button_connect)
        btn_connect.setOnClickListener{
            currentDevice = OnvifDevice(ipporttext.text.toString(), "", "")
            currentDevice.listener = this
            currentDevice.getDeviceInformation()
        }
        //Aygıtın ip,username,pass ları burda belirtiyoruz.



    }


    override fun requestPerformed(response: OnvifResponse) {

        //Bağlantı kurulduysa surfaceView e yönlendirme.
        //VLC MediaPlayer vs. burada.
        Log.d("Logx", response.parsingUIMessage)

        if(response.request.type == OnvifRequest.Type.GetDeviceInformation){
            currentDevice.getProfiles()
        }else if(response.request.type == OnvifRequest.Type.GetProfiles) {
            currentDevice.mediaProfiles.firstOrNull()?.let {
                currentDevice.getStreamURI(it)
            }
        }else if(response.request.type == OnvifRequest.Type.GetStreamURI){

            currentDevice.rtspURI?.let {uri ->

                 val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
                vlcVideoLibrary= VlcVideoLibrary(this,this,surfaceView)
                vlcVideoLibrary?.play(uri)


            }

        }



            //region

            /*  bağlantı dogru kuruldu mu kurulmadı mı kontrolleri burdan.

        Log.d("INFO",response.parsingUIMessage)

        if(response.request.type == OnvifRequest.Type.GetServices){
            currentDevice.getDeviceInformation()
        }else if(response.request.type == OnvifRequest.Type.GetDeviceInformation){
            currentDevice.getProfiles()
        }else if(response.request.type == OnvifRequest.Type.GetProfiles){
            currentDevice.getStreamURI(currentDevice.mediaProfiles.first())
        }

        //endregion
        */



    }

    override fun onComplete() {
        Toast.makeText(this, "Video Yükleniyor", Toast.LENGTH_LONG).show()
    }

    override fun onError() {
        Toast.makeText(this, "Hata", Toast.LENGTH_LONG).show()
    }

}


