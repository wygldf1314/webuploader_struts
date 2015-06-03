(function($) {
    $(function() {
        var log = (function() {
            var dom = $('#log');

            return function( str ) {
                dom.append('<p>' + str + '</p>')
            }
        })(),
        $wrap = $('#uploader'),
        $statusBar = $wrap.find( '.statusBar' ),
        $progress = $statusBar.find( '.progress' ).hide(),
        percentages = {},
        uploader;

        uploader = WebUploader.create({
            pick: '#filePicker',
            // 自动上传。
            auto: true,
            
            // 文件接收服务端。
            server: 'testUploadFile.action',
            chunked: true,
            chunkSize: 5 * 1024 * 1024,
            fileSizeLimit: 500 * 1024 * 1024,    // 500 M
            fileSingleSizeLimit: 500 * 1024 * 1024    // 500 M
        });
        
        
        
        uploader.onFileQueued = function( file ) {
          var start =  +new Date();
          // 返回的是 promise 对象
          this.md5File(file, 0, 1 * 1024 * 1024)

              // 可以用来监听进度
              .progress(function(percentage) {
                  // console.log('Percentage:', percentage);
              })

              // 处理完成后触发
              .then(function(ret) {
                  // console.log('md5:', ret);
                  
                  var end = +new Date();
                  log('Flash: md5 ' + file.name + ' cost ' + (end - start) + 'ms get value: ' + ret);
              });
      };
      
        
      uploader.onUploadProgress = function( file, percentage ) {
        var $li = $('#'+file.id),
            $percent = $li.find('.progress span');

        $percent.css( 'width', percentage * 100 + '%' );
        percentages[ file.id ][ 1 ] = percentage;
        updateTotalProgress();
    };
        
        
        //显示进度
        function updateTotalProgress() {
          var loaded = 0,
              total = 0,
              spans = $progress.children(),
              percent;

          $.each( percentages, function( k, v ) {
              total += v[ 0 ];
              loaded += v[ 0 ] * v[ 1 ];
          } );

          percent = total ? loaded / total : 0;


          spans.eq( 0 ).text( Math.round( percent * 100 ) + '%' );
          spans.eq( 1 ).css( 'width', Math.round( percent * 100 ) + '%' );
          updateStatus();
      }


    });
})(jQuery);