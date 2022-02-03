package com.hany.txtviewlayout
//git test
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.hany.txtviewlayout.databinding.ActivityViewerBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
var tlist = ArrayList<String>()
var page =0 //끝줄
var page2 =0 //앞줄
var plusPage = 0
var widthSize=1000
var heightSize=1000
var tlist1= ArrayList<String>()
class Viewer : AppCompatActivity() {
    val binding by lazy{ActivityViewerBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var sizeShared = getSharedPreferences("size", MODE_PRIVATE)
        widthSize=sizeShared.getInt("width",1000)
        heightSize=sizeShared.getInt("height",1000)

        val title = intent.getStringExtra("title")
        var strJsonTxt= title!!.let { readTextArray(it) }
        tlist= strJsonTxt!!.let { strChangeArray(it) }
        Log.d("jsss",strJsonTxt.toString())
//        readTextFile("${filesDir}/${title.toString()}")
//        Log.d("flll",file.toString())
        val pageShared = getSharedPreferences("page",Context.MODE_PRIVATE)
        page2 =pageShared.getInt(title,0)


        val customView=CustomView(this)
        binding.fl.addView(customView)

        binding.textView5.setOnClickListener{
            page2+=1
            val customView=CustomView(this)
            binding.fl.addView(customView)
            Log.d("aaaa99", "${page2}")
        }
        binding.textView2.setOnClickListener{
            page2-=1
            val customView=CustomView(this)
            binding.fl.addView(customView)
        }
        devidePage(tlist,title)
        var ttt=gettxt(title)
        tlist1=ttt!!.let { strChangeArray(it) }
    }
    //txt를 읽어와서
    fun gettxt(title: String): String? {

        val shared= getSharedPreferences("idText",Context.MODE_PRIVATE)
        Log.d("dskk",shared.toString())
        var jtxt = shared.getString(title,"")



        return jtxt

    }
    //받아온 문자열을 페이지에 들어갈만큼 잘라서 리스트에 저장하고 sp에 저장
    fun devidePage(tlist:ArrayList<String>,title: String){
        val shared= getSharedPreferences("idText",Context.MODE_PRIVATE)
        var tmp =shared.getString(title,"")
        if (tmp==""){
            var cd= widthSize
            var ch= heightSize

//            cd=800
//            ch=1000
            Log.d("faa",cd.toString())
            Log.d("faa",ch.toString())
            val paint = TextPaint()
            paint.color= Color.BLACK
            var tsize=40f
            paint.textSize=tsize
            var space =(tsize*1.5).toInt()
            var hei2= ch
            var startPage =0
            var startIndexArray=ArrayList<Int>(0)
            var txtArray= ArrayList<String>()
            for (line in 0..tlist.size-1){

                var wid = paint.measureText(tlist[line])
                Log.d("qpqp3",wid.toString())
                Log.d("qpqp4",tlist[line].toString())

                var lineHeight=(wid/ cd +1).toInt()*space
                Log.d("qpqp5",hei2.toString())
                if (hei2-lineHeight>200){
                    hei2-=lineHeight
                }
                else{
                    startIndexArray.add(line)
                    var txt=tlist.slice(startPage..line-1).joinToString (separator="\n" )
                    startPage = line
                    txtArray.add(txt)
                    Log.d("widx",txt.toString())
                    hei2=ch
                }
//

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
            var txt = arrayChangeStrJson(txtArray)
            Log.d("qqpp",txt)
            var editor = shared.edit()
            editor.putString(title,txt)
            editor.apply()
        }
    }





    override fun onStop() {
        super.onStop()
        val title = intent.getStringExtra("title")
        val pageShared = getSharedPreferences("page",Context.MODE_PRIVATE)
        val editor = pageShared.edit()
        editor.putInt(title,page2)
        editor.apply()
    }



    class CustomView(context: Context): View(context){
        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            canvas?.drawColor(Color.WHITE)

            var ch=canvas!!.height
            var cd= canvas!!.width
            val paint = TextPaint()
            paint.color= Color.BLACK
            var tsize=40f
            paint.textSize=tsize
//            var num_wd = wd/30f
//            Log.d("avv3",num_wd.toString())
//            var num_hei = hei/30f
            var hei2= ch
            var space =(tsize).toInt()
            val txt = tlist1[page2]
            Log.d("ttt3",txt)
            val textLayout = StaticLayout.Builder.obtain(txt, 0, txt.length, paint, cd) .setAlignment(
                Layout.Alignment.ALIGN_NORMAL) .setLineSpacing(tsize, 0.5f) .setIncludePad(true).build()

            canvas?.save()
            canvas?.translate(0f, 0f)
            textLayout.draw(canvas)
            canvas?.restore()
            //https://www.ienlab.net/entry/Android-Paint-Canvas%EC%97%90%EC%84%9C-%EC%9E%90%EB%8F%99%EC%9C%BC%EB%A1%9C-%EC%A4%84%EB%B0%94%EA%BF%88%ED%95%98%EA%B8%B0
            //canvas?.drawText (txt, 5, 100, 0f, 100f, paint)
        }
    }


    fun readTextFile(fullPath:String){

        val file = File(fullPath)
        if (!file.exists()) tlist
        val reader = FileReader(file)
        val buffer = BufferedReader(reader)
        var line: String? = buffer.readLine()
        while (line != null) {
            tlist.add(line)
            line = buffer.readLine()
        }
        buffer.close()
    }
    fun readTextArray(title:String):String{
        val shared = getSharedPreferences("txt",Context.MODE_PRIVATE)
        val txt =shared.getString(title,"").toString()
        Log.d("t132",txt)
        return txt
    }
    //json을 string으로 변환시킨 걸 배열로 다시 바꿔준다
    fun strChangeArray(jstxt:String):ArrayList<String>{
        var resultArr : ArrayList<String> = ArrayList()
        Log.d("jsss",jstxt)
        var arrJson = JSONArray(jstxt)
        Log.d("155",arrJson.toString())
        for(i in 0 until arrJson.length()){
            var arrJson2 = JSONObject(arrJson.optString(i))
            resultArr.add(arrJson2.optString("$i"))
        }

        return resultArr
    }

}