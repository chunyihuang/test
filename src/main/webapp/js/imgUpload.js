

		//上传图片
			$(".ft20 div").click(function(){
				$(this).addClass("active").siblings().removeClass("active");
			});
			$("#file0").change(function(){
				var objUrl = getObjectURL(this.files[0]);
				console.log("objUrl = "+objUrl);
				if(objUrl){
					$("#preview0").attr("src",objUrl);
				}
			});
			$("#file1").change(function(){
				var objUrl = getObjectURL(this.files[0]);
				console.log("objUrl = "+objUrl);
				if(objUrl){
					$("#preview1").attr("src",objUrl);
				}
			});
			$("#file2").change(function(){
				var objUrl = getObjectURL(this.files[0]);
				console.log("objUrl = "+objUrl);
				if(objUrl){
					$("#preview2").attr("src",objUrl);
				}
			});
			$("#file3").change(function(){
				var objUrl = getObjectURL(this.files[0]);
				console.log("objUrl = "+objUrl);
				if(objUrl){
					$("#preview3").attr("src",objUrl);
				}
			});
			$("#file4").change(function(){
				var objUrl = getObjectURL(this.files[0]);
				console.log("objUrl = "+objUrl);
				if(objUrl){
					$("#preview4").attr("src",objUrl);
				}
			});
			function getObjectURL(file){
				var url = null;
				if(window.createObjectURL!=undefined){
					url = window.createObjectURL(file);
				}else if(window.URL !=undefined){
					url = window.URL.createObjectURL(file) ;
				} else if (window.webkitURL!=undefined) {
					url = window.webkitURL.createObjectURL(file) ;
				}
				return url ;
			}