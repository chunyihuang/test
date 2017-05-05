//		验证账号是否合法
        
		$('#name').on('focusout',function(){
			if(checkeuser($('#name').val())){
				$(this).siblings('span').hide();
				$(this).attr('switch','true');
			}else{
				$(this).siblings('span').show();
				$(this).siblings('span').html('请输入以字母开头，数字，字母，下划线组成的4-16位！');
				$(this).attr('switch','false');
			}
		})
		$('#passsword').on('focusout',function(){
			$(this).siblings('span').show();
			if(checkpassword($(this).val())=='1'){
				$(this).siblings('span').css('color','red');
				$(this).attr('switch','true');
				$(this).siblings('span').html('密码安全性低');
			}
			if(checkpassword($(this).val())=='2'){
				$(this).siblings('span').css('color','limegreen');
				$(this).attr('switch','true');
				$(this).siblings('span').html('密码安全性中');
			}
			if(checkpassword($(this).val())=='3'){
				$(this).siblings('span').css('color','limegreen');
				$(this).attr('switch','true');
				$(this).siblings('span').html('密码安全性高');
			}
			if(checkpassword($(this).val())=='4'){
				$(this).siblings('span').css('color','red');
				$(this).attr('switch','false');
				$(this).siblings('span').html('密码输入有误，只能输入6-20个字母、数字、下划线');
			}
		})
		$('#passsword-new').on('focusout',function(){
			if($(this).val()==$('#passsword').val()){
				$(this).siblings('span').hide();
				$(this).attr('switch','true');
			}else{
				$(this).siblings('span').show();
				$(this).siblings('span').html('两次输入密码不一致！');
				$(this).attr('switch','false');
			}
		})
		function checkeuser(str){
			 var re=/^[a-zA-Z]\w{3,15}$/;
			 //字母、数字、下划线组成，字母开头，4-16位。
		    if(re.test(str)){  
		    	return true;
		    }  
		    else{  
		        return false;
		    } 
		}
		$('.submit').on('click',function(ev){
			if($('#passsword-new').attr('switch')=='true' && $('#name').attr('switch')=='true'
			&& $('#passsword').attr('switch')=='true'){
				$(this).siblings('span').hide();
				//验证通过1
				console.log($('#name').val()+$("#passsword-new").val()+$('#email').val())
				$.ajax({
	                type : "post",
	                url : "/player/register",
	                data : {
	                    "user":$('#name').val(),
	                    "password":$("#passsword-new").val(),
	                    email:$('#email').val()
	                },
	                async:true, 
	                success : function(result) {
	                    console.log(result);
	                    if (result.status ) {
	                        window.location.href = "index.html";
	                    } else {
	                    	console.log('失败');
	                    }
	                }
	            });
			}else{
				$(this).siblings('span').show();
				ev.preventDefault();
				return false;
			}
		})
		
		function checkpassword(str){
			var patrn=/^(\w){6,20}$/;
			if(patrn.exec(str)){
				 var re=/^(?:\d+|[a-zA-Z]+|[!@#$%^&*]+)$/;//纯数字或者纯字母纯特殊字符
				 var re1=/^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/;//字母数字特殊字符任意两种，强度中级
				if(re.test(str)){
					return '1';
				}
				if(re1.test(str)){
					return '2';
				}
				if(!re.test(str) && !re1.test(str)){
					return '3';
				}
			}else{
				return '4';
			}
		}
