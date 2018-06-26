# 图片选择器MultiMedia
##简单在xml中引用控件即可实现
在前辈（[RxGalleryFinal](https://github.com/FinalTeam/RxGalleryFinal)）的基础上加了简单封装，保留了原有功能
## 效果展示
![](https://github.com/wugod2015/MultiMedia/raw/master/art/Screenshot1.png)
### 引用multimedialibrary
直接引用multimedialibrary module源码，或者引用build/outputs下multimedialibrary-release.aar包
### 调用方式
```
<com.yckj.hhz.multimedialibrary.SelectImgRecyclerView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:is_crop="true"
            app:max_count="12"
            app:span_count="4"></com.yckj.hhz.multimedialibrary.SelectImgRecyclerView>
```
也可在代码中设置<br>
        selectImgGridView.setMaxCount(12);<br>
        selectImgGridView.setIsCrop(true);<br>
        selectImgGridView.setSpanCount(4);<br>
### 获取已选择图片
```
List<String> pathList = selectImgRecyclerView.getSelectedPathList();//地址
List<File> fileList = selectImgRecyclerView.getSelectedFileList();//文件
```
### 回调
```
selectImgGridView.setSelectImgListener(new SelectImgGridView.SelectImgListener() {
            @Override
            public void onOneResult(ImageRadioResultEvent resultEvent) {

            }

            @Override
            public void onMultipleResult(ImageMultipleResultEvent resultEvent) {

            }
        });
```
Android学习交流群：523487222<br>
（如果您觉得有用，欢迎加入，一起学习进步）<br>
[点击链接加入群【Android学习群】](http://jq.qq.com/?_wv=1027&k=2EKKOL6)<br>
![](https://github.com/wugod2015/MultiMedia/raw/master/art/erweima.png)
