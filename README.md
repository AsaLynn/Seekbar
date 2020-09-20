# Seekbar
Seekbar

# 效果图
![Image text](https://github.com/zhang721688/Seekbar/blob/master/image/image01.png)

# 依赖
```
implementation 'com.zxn.seekbar:seekbar:1.0.0'
```

# xml
```
<com.zxn.seekbar.RangeSeekbar 
    xmlns:seekbar="http://schemas.android.com/apk/res-auto"
    android:id="@+id/seekbar"
    android:layout_width="match_parent"
    android:layout_height="100dp"
    seekbar:leftCursorBackground="@mipmap/ahlib_combined_shape_down"
    seekbar:markTextArray="@array/markArray"
    seekbar:rightCursorBackground="@mipmap/ahlib_combined_shape_top"
    seekbar:seekbarColorSelected="#3396FB"
    seekbar:seekbarHeight="5dp"
    seekbar:spaceBetween="30dp"
    seekbar:textColorNormal="#bbbbbb"
    seekbar:textColorSelected="#bbbbbb"
    seekbar:textSize="15sp" />
```

# 属性
- leftCursorBackground
左侧游标icon
- rightCursorBackground
右侧侧游标icon
- markTextArray
数据
- seekbarColorSelected
选中部分进度条颜色
- seekbarHeight
进度条高度
- spaceBetween
刻度和滑动条之间的间隙
- textColorNormal
正常刻度文字颜色
- textColorSelected
选中的刻度文字颜色


# 代码
```
mSeekBar = (RangeSeekbar) view.findViewById(R.id.seekbar);
mSeekBar.setOnCursorChangeListener(new RangeSeekbar.OnCursorChangeListener() {
    @Override
    public void onLeftCursorChanged(int location, String textMark) {

    }

    @Override
    public void onRightCursorChanged(int location, String textMark) {

    }
});
mSeekBar.setLeftSelection(2);
mSeekBar.setRightSelection(4);
```
# 系统SeekBar属性分析
