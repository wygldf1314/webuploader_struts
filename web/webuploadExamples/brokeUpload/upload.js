            var chunkSize = 5 * 1024 * 1024;        //分块大小
            var md5Mark = null;
            var itemsStatus = new Array();

            WebUploader.Uploader.register({
                "before-send-file": "beforeSendFile",
                "before-send": "beforeSend",
                "after-send-file": "afterSendFile"
            }, {
                beforeSendFile: function(file){
                    //秒传验证
                    var task = new $.Deferred();
                    var start = new Date().getTime();
                    (new WebUploader.Uploader()).md5File(file, 0, 10*1024*1024).progress(
                        function(percentage){
                          console.log(percentage);
                    }).then(function(val){
                        console.log("总耗时: "+((new Date().getTime()) - start)/1000);
                        md5Mark = val;
                        $.ajax({
                            type: "POST",
                            url: "checkMD5.action",
                            data: {
                                status: "md5Check",
                                md5: val,
                                fileName: file.name
                            },
                            async: false,
                            cache: false,
                            timeout: 1000, //todo 超时的话，只能认为该文件不曾上传过
                            dataType: "json"
                        }).then(function(data, textStatus, jqXHR){
                            if(data.ifExist){  
                              //若存在，则返回失败给WebUploader，表明该文件不需要上传
                                task.reject();
                                if(confirm("当前文件已经存在，确定要覆盖当前文件？")) {
                                }else{
                                  return;
                                }
                                uploader.skipFile(file);
                                file.path = data.path;
                                UploadComlate(file);
                                
                            }else{
                                task.resolve();
                                itemsStatus = data.chunkFileNames;
                            }
                        }, 
                        function(jqXHR, textStatus, errorThrown){//任何形式的验证失败，都触发重新上传
                          task.resolve();
                        });
                    });
                    return $.when(task);
                },
                beforeSend: function(file){
                    //分片验证是否已传过，用于断点续传
                    var task = new $.Deferred();

                    if(itemsStatus.indexOf(""+file.chunk) != -1){   //若存在，返回失败给WebUploader，表明该分块不需要上传
                        task.reject();
                    }else{
                        task.resolve();
                    }

                    return $.when(task);
                },
                afterSendFile: function(file){
                    var chunksTotal = 0;
                    if((chunksTotal = Math.ceil(file.size/chunkSize)) >= 1){
                        //合并请求
                        var task = new $.Deferred();
                        $.ajax({
                            type: "POST",
                            url: "mergeFileByMd5.action",
                            data: {
                                status: "chunksMerge",
                                chunks: chunksTotal,
                                ext: file.ext,
                                md5: md5Mark,
                                fileName: file.name
                            },
                            cache: false,
                            dataType: "json"
                        }).then(function(data, textStatus, jqXHR){
                            //todo 检查响应是否正常

                            task.resolve();
                            file.path = data.path;
                            UploadComlate(file);

                        }, 
                        function(jqXHR, textStatus, errorThrown){
                            task.reject();
                        });

                        return $.when(task);
                    }else{
                        UploadComlate(file);
                    }
                }
            });

      var uploader = WebUploader.create({
        swf: "Uploader.swf",
        server: "uploadFileByMd5.action",
        pick: "#picker",
        resize: false,
        dnd: "#theList",
        paste: document.body,
        disableGlobalDnd: true,
        thumb: {
          width: 100,
          height: 100,
          quality: 70,
          allowMagnify: true,
          crop: true
        },
        compress: false,
        prepareNextFile: true,
        chunked: true,
        chunkSize: chunkSize,
        threads: true,
        fileNumLimit: 1,
        fileSingleSizeLimit: 20 * 1000 * 1024 * 1024,
        duplicate: true
      });
      
      uploader.on('uploadBeforeSend', function(object,data,headers) {
        data.chunk = object.chunk;
        data.uploadFileStatus = itemsStatus;
        data.md5 = md5Mark;
      });
        
      uploader.on("fileQueued", function(file){
        (new WebUploader.Uploader()).md5File(file, 0, 10*1024*1024).progress().then(function(val){
          md5Mark = val;
        });
        $("#theList").append('<li id="'+file.id+'">' +
          '<img /><span>'+file.name+'</span><span class="itemUpload">上传</span><span class="itemStop">暂停</span><span class="itemDel">删除</span>' +
          '<div class="percentage"></div>' +
        '</li>');
        
        var $img = $("#" + file.id).find("img");
        
        uploader.makeThumb(file, function(error, src){
          if(error){
            $img.replaceWith("<span>不能预览</span>");
          }

          $img.attr("src", src);
        });
        
      });
      
      $("#theList").on("click", ".itemUpload", function(){
          uploader.upload();
          //"上传"-->"暂停"
          $(this).hide();
          $(".itemStop").show();
      });

      $("#theList").on("click", ".itemStop", function(){
          uploader.stop(true);
  
          //"暂停"-->"上传"
          $(this).hide();
          $(".itemUpload").show();
      });

      //todo 如果要删除的文件正在上传（包括暂停），则需要发送给后端一个请求用来清除服务器端的缓存文件
      $("#theList").on("click", ".itemDel", function(){
        uploader.removeFile($(this).parent().attr("id")); //从上传文件列表中删除

        $(this).parent().remove();  //从上传列表dom中删除
      });
      
      uploader.on("uploadProgress", function(file, percentage){
        $("#" + file.id + " .percentage").text(percentage * 100 + "%");
      });

      function UploadComlate(file){
          console.log(file);
          
          $("#" + file.id + " .percentage").text("上传完毕");
          $(".itemStop").hide();
          $(".itemUpload").hide();
          $(".itemDel").hide();
      }