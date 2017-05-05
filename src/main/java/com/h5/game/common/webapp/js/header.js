var height_window=$(window).height();
$('header .personal-info-nav').css('height',height_window+'px');
window.onresize=function(){
	var height_window=$(window).height();
	$('header .personal-info-nav').css('height',height_window+'px');
}
//PC端导航条的交互效果
$('#nav-main-menu .havechildnav').on('mouseover',function(){
	$(this).find('.menu-level-two').addClass('active');
})
$('#nav-main-menu .havechildnav').on('mouseout',function(){
	$(this).find('.menu-level-two').removeClass('active');
})

$('nav .user a').eq(0).on('click',function(){
	$(this).siblings('.menu-level-one').fadeToggle(200);
})

//		手机平板屏幕的导航交互效果
$('header .personal-info-nav').on('click',function(){
	$(this).hide();
})
$('header .menu').on('click',function(){
	$('header .personal-info-nav').show();
})
$('header .personal-info-nav .nav').on('click',function(){
	return false;
})
$('header .item-level-one').on('click',function(){
	var m=$(this).index();
	$('header .item-level-one .glyphicon').each(function(i,elme){
		if($(elme).closest('.item-level-one').index()!=m){
			$(elme).removeClass('active');
		}
	})
	$(this).find('.glyphicon').toggleClass('active');
	$(this).siblings('.menu-level-two').each(function(i,elme){
		if($(elme).index()!=m+1){
			$(elme).slideUp(300);
		}
	})
	if($(this).next().attr('class')=='menu-level-two'){
		$(this).next().slideToggle(300)
	}
})

//		第一页的导航小屏幕交互效果
$('#section0 .small-screen .item-level-one').on('click',function(){
	$(this).toggleClass('active');
	if($(this).next().attr('class')=='menu-level-two'){
		$(this).next().slideToggle(300);
	}
})