package com.hany.txtviewlayout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.hany.txtviewlayout.databinding.ActivityMainBinding
import java.io.*
import org.json.JSONObject

import org.json.JSONArray




class MainActivity : AppCompatActivity() {
    val REQ_OPEN_FILE = 101
    val binding by lazy{ActivityMainBinding.inflate((layoutInflater))}

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        widthSize =binding.cl.measuredWidth
        heightSize=binding.cl.measuredHeight
        Log.d("wdwd",widthSize.toString())
        val shared = getSharedPreferences("size", Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putInt("width",widthSize)
        editor.putInt("height",heightSize)
        editor.apply()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button2.setOnClickListener {
            openFile("tt", "text/plain")
        }
    }


    override fun onStart() {
        super.onStart()
        super.onRestart()
        val data:MutableList<Memo> =loadData()
        var adapter = CustomAdapter()
        adapter.listData= data
        binding.recyclerView.adapter =adapter
        binding.recyclerView.layoutManager= GridLayoutManager(this,3)
    }


    //SAF를 통해 외부 저장소에서 txt파일을 골라서 내부 저장소에 저장한다
    fun openFile(filename:String, mimeType:String) {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
        }
        startActivityForResult(intent, REQ_OPEN_FILE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                REQ_OPEN_FILE -> {
                    data?.data?.also { uri ->
                        var name =dumpImageMetaData(uri)
                        val shared = getSharedPreferences("title모음", Context.MODE_PRIVATE)
                        val editor = shared.edit()
                        editor.putString(name,name)
                        editor.apply()
                        Log.d("urii","$name")
//                        var a = readTextFromUri(uri)
                        var arrayTxt=readTextFromUri2(uri)
                        //array를 json으로 바꾼후 string으로바꾼다
                        var jsonTxt= arrayChangeStrJson(arrayTxt)

                        val shared2 = getSharedPreferences("txt", Context.MODE_PRIVATE)
                        val editor2 = shared2.edit()
                        editor2.putString(name,jsonTxt)
                        editor2.apply()

                        val shared3 = getSharedPreferences("page", Context.MODE_PRIVATE)
                        val editor3 = shared3.edit()
                        editor3.putInt(name,0)
                        editor3.apply()
//                        var outputFile : FileOutputStream = openFileOutput(name, MODE_PRIVATE)
//                        outputFile.write(a.toByteArray())
//                        outputFile.close()
//                        writeTextFile(filesDir.toString(), "$name", a)
                    }


                }
            }
        }
    }
    fun arrayChangeStrJson(b:ArrayList<String>):String{
        val jArray = JSONArray() //배열
        for (i in 0 until b.size) {
            val sObject = JSONObject() //배열 내에 들어갈 json
            sObject.put("${i}", b.get(i))
            jArray.put(sObject)
        }
        return jArray.toString()
    }
    //파일 이름을 체크하는 코드
    @SuppressLint("Range")
    fun dumpImageMetaData(uri: Uri):String {
        val cursor: Cursor? = contentResolver.query( uri, null, null, null, null, null)
        var displayName=""

        cursor?.use {
            if (it.moveToFirst()) {
                 displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))  // 1
            }
        }
        return displayName
    }
//내부 저장소에 저장
    fun writeTextFile(directory:String,filename: String,content:String){
        val dir = File(directory)
        if(!dir.exists()){
            dir.mkdir()
        }
        val writer = FileWriter(directory +"/"+filename)
        val buffer = BufferedWriter(writer)
        buffer.write(content)
        buffer.close()
    }
    fun readTextFromUri(uri: Uri): String {
        val stringBuilder = StringBuilder()
        contentResolver.openInputStream(uri)?.use { inputStream ->

            BufferedReader(InputStreamReader(inputStream, "utf-16le")).use { reader ->
                var line: String? = reader.readLine()

                while (line != null) {
                    stringBuilder.append(line+"\n")
                    line = reader.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }

    //array 리턴
    fun readTextFromUri2(uri: Uri): ArrayList<String> {
        var txxt=ArrayList<String>()
        contentResolver.openInputStream(uri)?.use { inputStream ->

            BufferedReader(InputStreamReader(inputStream, "utf-16LE")).use { reader ->
                var line: String? = reader.readLine()

                while (line != null) {
                    txxt.add(line)
                    line = reader.readLine()
                }
            }
        }
        return txxt
    }
    fun loadData(): MutableList<Memo>{
        val data: MutableList<Memo> = mutableListOf()

        val shared = getSharedPreferences("title모음",Context.MODE_PRIVATE)
        var title=shared.all.values
        var it = title.iterator()
        Log.d("ttle",title.toString())
        while(it.hasNext()){
            val title = it.next()
            val memo = Memo(title.toString())
            data.add(memo)

        }

        return data

    }
}