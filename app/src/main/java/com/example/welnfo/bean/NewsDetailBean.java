package com.example.welnfo.bean;

import java.util.List;

public class NewsDetailBean {

    /**
     * body : <div class="main-wrap content-wrap">
     <div class="headline">

     <div class="img-place-holder"></div>



     </div>

     <div class="content-inner">




     <div class="question">
     <h2 class="question-title">为什么CPU流水线设计的级越长，完成一条指令的速度就越快？</h2>

     <div class="answer">

     <div class="meta">
     <img class="avatar" src="http://pic3.zhimg.com/v2-bac9887ebb075156c30a391d37e393f2_is.jpg">
     <span class="author">木头龙，</span><span class="bio">开了个新专栏，收录些老回答</span>
     <a href="https://www.zhihu.com/question/20180643/answer/1269060365" class="originUrl" hidden>查看知乎原文</a>
     </div>

     <div class="content">
     <blockquote>请问提高主频和流水线有什么样的关系？<br>流水线设计的步（级）越长，完成一条指令的速度就越快。</blockquote>
     <p>这两个问题一起回答好了。</p>
     <p>既然是流水线，就用流水线举个例子好了。假设一条工厂的流水线，需要给零件拧 4 种不同规格的螺丝，每种螺丝 2 个，工人拧一个螺丝需要 5 秒（工人拧螺丝的时候流水线需要暂停等待），流水线移动一格需要 5 秒。</p>
     <p>对比一下流水线分 4 级，每个工人拧 2 个同规格的螺丝，和流水线分 8 级，每个工人拧 1 个螺丝相比：</p>
     <ul><li>4 级流水：完成一个工件的总时间：(5+5+5)×4=60 秒；每 15 秒钟产出一个工件。</li>
     <li>8 级流水：完成一个工件的总时间：(5+5)×8=80 秒；每 10 秒钟产出一个工件。</li>
     </ul><p>流水线的移动速度并没有加快，但是通过更多级的流水线，可以使得流水线的暂停等待时间更短，从 1/15 Hz 提高到 1/10 Hz，频率更高。</p>
     <p>如果我们再假设一个机制，就是当产出的工件达到一定数量后，就马上改为生产另一种工件，4 级流水只要 60 秒就能产出第一个工件，而 8 级流水需要 80 秒。如果每种工件每生产 3 个就更换一种，那么 4 级流水生产一种工件需要 60+15×2=90 秒，8 级流水生产一种工件需要 80+10×2=100 秒，反而更慢。这就是过长的流水线带来的性能下降问题，Intel 的 Netburst 架构，AMD 的 Bulldozer 架构都存在这个问题。</p>
     <blockquote>文中还说“实质是以时间换取空间”</blockquote>
     <p>这个先看原文：</p>
     <blockquote>超标量（superscalar）是指在 CPU 中有一条以上的流水线，并且每时钟周期内可以完成一条以上的指令，这种设计就叫超标量技术。 其实质是以空间换取时间。而超流水线是通过细化流水、提高主频，使得在一个机器周期内完成一个甚至多个操作，其实质是以时间换取空间。</blockquote>
     <p>在上面的例子中，设置 8 级流水的代价是每个工件的完成时间增加了 20 秒，这就是这段话中以时间换取空间的意思。</p>
     <blockquote>扩展指令集中指令的条数是否受到了制造工艺的限制，如果没有的话，为什么不集成更多的扩展指令集呢。</blockquote>
     <p>CPU 一直在集成更多的扩展指令。Intel 几乎每一代 CPU 都会增加一条或者多条指令；某一代甚至是增加整个指令集，例如大家经常听到的 x87，MMX，SSE，AVX 等。不过事实上，因为 CPU 里面都是二进制数据，因此理论上一个通用处理器，只要支持最基本的几种二进制运算就可以实现任何计算。当然，这样的处理速度会很慢，因为一个很常见的操作，就需要转换分解成很多步基本运算才能完成。所谓的扩展指令，就是把一些常见的基本指令组合用专用的硬件实现，达到一个或者几个时钟周期，就可以计算出需要几百上千步基本运算才能完成的计算。</p>
     <p>但是这样的组合是无穷无尽的，每一条指令都需要用额外的晶体管来实现，虽然每一次工艺升级，都可以在同样大小的晶圆面积上摆放下更多的晶体管，但具体到某一代工艺，良率和成本限制所决定的可接受最大芯片面积，能容纳的晶体管数量总是有限的。只能由 CPU 厂商根据业界发展预测将来哪一些指令组合会更频繁的被使用，并把这些组合用硬件电路实现，固化为新的扩展指令。</p>
     <p>举例来说，我写一个软件，经常要计算 A×B÷C 这样的组合，但 CPU 厂家觉得这只是我一个人的情况，所以不会做一个这样的扩展指令；但 A×B+C 因为是常见的矩阵运算，有很多人用，所以就做了一个 FMA 指令集。</p>
     <blockquote>指令是否就是指指令集中的指令，</blockquote>
     <p>是的</p>
     <blockquote>他们与终端的软件程序有什么关系？</blockquote>
     <p>软件程序就是一组按照特定顺序排列以完成某种特定任务的指令代码，以及运行这些指令所必须的数据，按照操作系统约定的某种结构所组成的一组二进制代码。</p>
     <blockquote>软件程序的运行是否全部转化为这些指令，随后让这些指令到处理器的流水线中进行执行？</blockquote>
     <p>是的，CPU 流水线的第一阶段就是 CPU 的取指单元从内存中载入一段程序，并且按照操作系统约定的格式，从中提取出按顺序排列的一串指令代码。第二阶段是 CPU 的译码单元把这些代码分解成具体的指令以及指令操作对象（寄存器、内存地址或者立即数），某些复杂指令可能还需要再进一步拆分成多条内部指令。</p>
     </div>
     </div>


     <div class="view-more"><a href="https://www.zhihu.com/question/20180643">查看知乎讨论<span class="js-question-holder"></span></a></div>

     </div>


     </div>
     </div><script type=“text/javascript”>window.daily=true</script>
     * image_hue : 0x476652
     * image_source : bbAAER / CC0
     * title : 为什么 CPU 流水线设计的级越长，完成的速度就越快？
     * url : https://daily.zhihu.com/story/9724973
     * image : https://pic2.zhimg.com/v2-14ad685af4bbf9f0e16151c83e60304d.jpg
     * share_url : http://daily.zhihu.com/story/9724973
     * js : []
     * ga_prefix : 061707
     * images : ["https://pic1.zhimg.com/v2-211dc74bae24525d0753ccadb81e72c8.jpg"]
     * type : 0
     * id : 9724973
     * css : ["http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3"]
     */

    private String body;
    private String image_hue;
    private String image_source;
    private String title;
    private String url;
    private String image;
    private String share_url;
    private String ga_prefix;
    private int type;
    private int id;
    private List<?> js;
    private List<String> images;
    private List<String> css;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_hue() {
        return image_hue;
    }

    public void setImage_hue(String image_hue) {
        this.image_hue = image_hue;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<?> getJs() {
        return js;
    }

    public void setJs(List<?> js) {
        this.js = js;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }
}
