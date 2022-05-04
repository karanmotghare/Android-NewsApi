package com.example.newsapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class NewsAdapter(context: Context, arrayList:ArrayList<Data>): BaseAdapter() {

    var arrayList: ArrayList<Data> = ArrayList()
    var context: Context?

    init {
        this.arrayList = arrayList
        this.context = context
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(p0: Int): Any {
        return arrayList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View
        val holder: ViewHolder
        val inflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if(p1==null){
            holder = ViewHolder()
            view = inflater.inflate(R.layout.view_res,p2,false)
            holder.sectionName = view.findViewById(R.id.txt_view1)
            holder.webTitle = view.findViewById(R.id.txt_view2)

            view.tag = holder
        }else{
            view= p1
            holder = p1.tag as ViewHolder
        }
        val sectionNamevalue :TextView? = holder.sectionName
        sectionNamevalue?.text = arrayList[p0].sectionName

        val titleValue: TextView? = holder.webTitle
        titleValue?.text = arrayList[p0].webTitle

        return view
    }

    class ViewHolder{
        var sectionName: TextView? =null
        var webTitle: TextView? =null
    }

}