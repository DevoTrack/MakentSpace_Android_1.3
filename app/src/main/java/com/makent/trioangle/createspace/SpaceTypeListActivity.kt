package com.makent.trioangle.createspace

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.createspace.genericadapter.JavaViewHolderFactory
import com.makent.trioangle.createspace.model.hostlisting.basics.BasicStepsModel
import com.makent.trioangle.placesearch.RecyclerItemClickListener

class SpaceTypeListActivity : AppCompatActivity() {
   lateinit var ivBack:ImageView
    lateinit var rvSpaceTypes:RecyclerView

    lateinit var basicStepsModel: BasicStepsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_type_list)

        Log.e("SpaceType","SpaceType List");

        ivBack=findViewById(R.id.iv_back)
        rvSpaceTypes=findViewById(R.id.rv_space_types)
        ivBack.setOnClickListener {
            onBackPressed()
        }

        basicStepsModel=getIntent().getExtras()!!.getSerializable("spaceType") as BasicStepsModel
        rvSpaceTypes.layoutManager = LinearLayoutManager(this)

        val myAdapter = object : GenericAdapter<Any>(basicStepsModel.spaceTypes) {
            override fun onItemClick(pos: Int, id: String, name: String) {

            }

            override fun getLayoutId(position: Int, obj: Any): Int {

                return R.layout.list_items
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return JavaViewHolderFactory.create(view,viewType)
            }
        }
        rvSpaceTypes.adapter=myAdapter
        rvSpaceTypes!!.addOnItemTouchListener(
                RecyclerItemClickListener(this!!, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        //Write your code here
                        intent.putExtra("selectedItem",basicStepsModel.spaceTypes.get(position).name)
                        intent.putExtra("selectedItemId",basicStepsModel.spaceTypes.get(position).id)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                        println("Position "+position)
                    }
                }))
    }

}
