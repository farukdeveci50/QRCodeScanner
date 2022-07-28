package com.example.qrcode

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.qrcode.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var codescanner : CodeScanner
    private lateinit var tasarim : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasarim = ActivityMainBinding.inflate(layoutInflater)
        setContentView(tasarim.root)

        tasarim.button.setOnClickListener {
            val intent = Intent(this@MainActivity,DetayActivity::class.java)
            intent.putExtra("mesaj",tasarim.textViewDeger.text.toString())
            startActivity(intent)
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),123)
        }else{
            startScanning()
        }

    }

    private fun startScanning() {
        val scannerView: CodeScannerView = tasarim.scannerView
        codescanner = CodeScanner(this,scannerView)
        codescanner.camera = CodeScanner.CAMERA_BACK
        codescanner.formats = CodeScanner.ALL_FORMATS

        codescanner.autoFocusMode = AutoFocusMode.SAFE
        codescanner.scanMode = ScanMode.SINGLE
        codescanner.isAutoFocusEnabled = true
        codescanner.isFlashEnabled = false

        codescanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this,"Scan Result: ${it.text}",Toast.LENGTH_SHORT).show()
                tasarim.textViewDeger.text = it.text
            }
        }
        codescanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this,"Camera initialization error: ${it.message}",Toast.LENGTH_SHORT).show()
            }
        }
        tasarim.scannerView.setOnClickListener {
            codescanner.startPreview()
            tasarim.textViewDeger.text = ""
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 123){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Camera permission granted",Toast.LENGTH_SHORT).show()
                startScanning()
            }else{
                Toast.makeText(this,"Camera permission denied",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(::codescanner.isInitialized){
            codescanner?.startPreview()

        }
    }

    override fun onPause() {
        if(::codescanner.isInitialized){
            codescanner?.releaseResources()

        }
        super.onPause()

    }
}