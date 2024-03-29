# AndroidCommonLibrary
个人使用的基本仓库

### 支持功能
* 使用Fresco加载图片
* 集成Rxjava2
* 使用Retrofit2完成网络请求加载
* 提供多种工具类
* 支持沉浸式
* 支持多种通知栏效果
* 支持权限判断
* 集成腾讯xlog日志记录框架
* 集成MMKV——基于 mmap 的高性能通用 key-value 组件
* 使用SmartRefreshLayout完成下拉上拉加载
* 集成侧滑删除RecyclerView子项控件

![SwipeDelMenuLayout](https://github.com/mcxtzhang/SwipeDelMenuLayout/raw/master/gif/ItemDecorationIndexBar_SwipeDel.gif)

* 更简洁的实现RecyclerView分割线效果

![RecyclerItemDecoration](https://raw.githubusercontent.com/magiepooh/RecyclerItemDecoration/master/art/demo_vertical.gif)

![RecyclerItemDecoration](https://raw.githubusercontent.com/magiepooh/RecyclerItemDecoration/master/art/demo_horizontal.gif)

* 集成RecyclerView元素加载、增删动画控件
* 集成仿IOS SwitchButton控件
* 集成vlayout

![vlayout](https://camo.githubusercontent.com/2b947a15f5502af5a4639a5927d68052ccfb54a3/687474703a2f2f696d67332e746263646e2e636e2f4c312f3436312f312f31623962666234323030393034376637356365653038616537343135303564653263373461633061)

* 集成效果极佳的轮播广告图控件

![ViewPagerTransforms](https://github.com/saiwu-bigkoo/Android-ConvenientBanner/raw/master/preview/convenientbannerdemo.gif)

* 提供统一的网络加载提示框
* 集成滚轮控件
* 集成圆形进度条控件
* 集成流式布局控件
* 集成自带删除功能的输入框控件
* 集成ViewPagerTransforms，viewpager切换动画

![ViewPagerTransforms](https://camo.githubusercontent.com/8dabc7f764609bd8fbe9a7c594251e0e5d20ebdc/687474703a2f2f692e696d6775722e636f6d2f72766845326e732e676966)

* 集成微信WCDB数据库
* 集成CircleIndicator，ViewPager指示器控件

![CircleIndicator](https://github.com/ongakuer/CircleIndicator/raw/master/screenshot.gif)

* 集成DiscreteScrollView，以当前item为中心的可滚动列表

![DiscreteScrollView](https://github.com/yarolegovich/DiscreteScrollView/raw/master/images/cards_shop.gif)

![DiscreteScrollView](https://github.com/yarolegovich/DiscreteScrollView/raw/master/images/cards_weather.gif)

* 解决SwipyRefreshLayout滑动冲突
* 集成腾讯浏览服务
* 新增跑马灯特效

![MarqueeViewLibrary](https://github.com/gongwen/MarqueeViewLibrary/raw/master/screenshot/screen_shot.gif)

* 支持自由定制外观、拖拽消除的MaterialDesign风格Android BadgeView

![BadgeView](https://github.com/qstumn/BadgeView/raw/master/demo_gif.gif)

* 支持预加载时显示view占位符的效果

![Broccoli](https://github.com/samlss/Broccoli/blob/master/screenshots/screenshot1.gif)

* 支持多个滑动布局(RecyclerView、WebView、ScrollView等)和普通控件(TextView、ImageView、LinearLayout、自定义View等)持续连贯滑动的容器,它使所有的子View像一个整体一样连续顺畅滑动。并且支持布局吸顶功能

![ConsecutiveScroller](https://camo.githubusercontent.com/72dcec1524ae45fd9cf157ac96af8b40465ef01434d2855d584d8e40874db90b/68747470733a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f323336353031302d663262363464323030323264323536362e6769663f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970)

* 支持包含强制与非强制的app升级功能
* 支持自定义底部弹出框
* 支持权限判断

### 适配注意点
![Android 11 变更及适配攻略](https://blog.csdn.net/qq_17766199/article/details/115351949)

* 使用 gradle build --scan 去分析编译数据