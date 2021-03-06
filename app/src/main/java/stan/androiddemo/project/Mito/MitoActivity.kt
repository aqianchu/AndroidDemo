package stan.androiddemo.project.Mito

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_mito.*
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.PageAdapter
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageCatInfo
import stan.androiddemo.project.Mito.Model.ImageSetInfo
import stan.androiddemo.project.Mito.Model.Resolution

class MitoActivity : AppCompatActivity() {

    lateinit var mAdapter:PageAdapter
    var currentSelectCat = 0
    var imageCat:ImageCatInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mito)
        title = ""
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher)
        }
       // 全部	美女	性感	明星	风光	卡通	创意	汽车	游戏	建筑	影视	植物	动物	节庆	可爱	静物	体育	日历	唯美	其它	系统	动漫	非主流	小清新
        val titles = arrayListOf("全部",	"美女","性感","明星","风光","卡通","创意","汽车","游戏","建筑","影视","植物","动物",
                "节庆","可爱","静物","体育","日历","唯美","其它","系统","动漫","非主流","小清新")
        val fragments = arrayListOf<ImageFragment>()
        for (i in 0 until titles.size){
            val fra = ImageFragment.createFragment()
            val bundle = Bundle()
            bundle.putString("cat",titles[i])
            fra.arguments = bundle
            fragments.add(fra)
        }
        mAdapter = PageAdapter(supportFragmentManager,fragments,titles)
        viewPager.adapter = mAdapter
        tabLayout.setupWithViewPager(viewPager)



        navigation_view.setNavigationItemSelectedListener { //组这些menu添加事件
            val id = it.itemId
            when(id){
                R.id.nav_computer_mito->{
                    currentSelectCat = 0
                    mAdapter.mFragments.map { (it as ImageFragment).refreshCat(0) }
                }
                R.id.nav_tablet_mito->{
                    currentSelectCat = 1
                    mAdapter.mFragments.map { (it as ImageFragment).refreshCat(1) }
                }
                R.id.nav_phone_mito->{
                    currentSelectCat = 2
                    mAdapter.mFragments.map { (it as ImageFragment).refreshCat(2) }
                }
                R.id.nav_essential_mito->{
                    currentSelectCat = 3
                    mAdapter.mFragments.map { (it as ImageFragment).refreshCat(3) }
                }
                R.id.nav_my_collect->{
                    val intent = Intent(this,CollectActivity::class.java)
                    startActivityForResult(intent,0x00001)
                }
                R.id.nav_about_mito->{
                    val intent = Intent(this,AboutMitoActivity::class.java)
                    startActivityForResult(intent,0x00001)
                }
                R.id.nav_search_mito->{
                    val intent = Intent(this,ImageSearchActivity::class.java)
                    startActivityForResult(intent,0x00001)
                }
                R.id.nav_subject_mito->{
                    val intent = Intent(this,ImageSubjectActivity::class.java)
                    startActivityForResult(intent,0x00001)
                }
            }
            mAdapter.mFragments.map { (it as ImageFragment).currentResolution = Resolution.wholeResolution }
            getImageSet()
            drawer_layout_fixed.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

        navigation_view.menu.getItem(0).isChecked = true//默认选中第一项


        float_button_filter.setOnClickListener {
            if (imageCat == null){
                return@setOnClickListener
            }
            val frag =FilterMitoDialogFragment.create(imageCat!!)
            frag.isCancelable = true
            frag.show(supportFragmentManager,"filterFragment")
            frag.resolutionBLock = {resolution:Resolution ->
                mAdapter.mFragments.map { (it as ImageFragment).refreshWithResolution(resolution) }
                frag.dismiss()
            }
        }

        getImageSet()


    }

    private fun  getImageSet(){
        ImageCatInfo.imageCats(currentSelectCat,"全部","全部") { v : ResultInfo ->
            runOnUiThread {

                if (v.code != 0) {
                    Toast.makeText(this,v.message, Toast.LENGTH_LONG).show()
                    return@runOnUiThread
                }
                imageCat = v.data as ImageCatInfo?
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home->{
                drawer_layout_fixed.openDrawer(GravityCompat.START)
            }
            //不对，不是在这里触发的

        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        navigation_view.menu.getItem(currentSelectCat).isChecked = true
    }

    override fun onBackPressed() {
        if (drawer_layout_fixed.isDrawerOpen(GravityCompat.START)){
            drawer_layout_fixed.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }

    }
}
