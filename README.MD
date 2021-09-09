# Jetpack Demo
### 已集成
- ViewBinding
- Room
- WorkManager
  - OneTimeWorkRequest
  - PeriodicWorkRequest
- liveData+viewModel
- DrawLayout
----
### others
- Use Activity-alias to change app name and icon
----
### Custom view
- How to use bezier
- How to resolve nested view scroll clash -> to see BezierView onTouchEvent
- Fake EBookReader
- QQ msg item
- QQ DrawLayout && change MainView
- FireworkView -> Surface View draw UI not in UI-Thread
- CountDownClock
- PathMeasure -> clip path segment to draw
- NestedScrollingChild & Parent
----
# Note
- PathMeasure中getSegment方法 startWithMoveTo参数
    - true：下一条线段如果起点不在上一条的终点 就先调用moveTo移动到下一条线段的起点再继续绘制。能按照原先的需求绘制
    - false:把上一条线段的终点当作这条线段的起点绘制 线段就会改变
    - 如：
    ```java
     mPath.moveTo(10,10);
     mPath.lineTo(10,300);
     mPath.moveTo(300,10);
     mPath.lineTo(300,300);
    ```
    画出来的效果应该是两条竖线 如果第二条线段用pathMeasure截取的时候startWithMoveTo参数设置false 就会变成 L 
