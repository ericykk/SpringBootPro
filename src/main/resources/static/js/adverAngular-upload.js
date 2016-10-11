(function(){
    /*
     外部接口：
     file: 上传的文件对象
     uploadUrl: 上传url
     opts: 上传配置
     {
     progressCallback：进度条事件
     successCallback：上传成功事件
     errorCallback： 上传出错时间
     maxsize：上传最大尺寸，单位M
     extReg：限制文件上传类型， 类型为正则
     params: 请求参数
     }
     */

    function FileUploader(file, uploadUrl, opts){
        if(typeof file === undefined || typeof file === null){
            throw 'please pass file object.';
        }

        this.file = file;
        this.uploadUrl = uploadUrl;
        this.opts = opts || {};

        this.xhr = new XMLHttpRequest();
        this._bindXhrEvent();
        this.upload();
    }

    FileUploader.prototype = {
        upload: function(){
            var checkResult = this._preCheck();
            if(checkResult === ''){			//可以上传
                this.xhr.open("POST", this.uploadUrl);
                this.xhr.send(this._buildParams());
            }else{		//预检查出错
                console.error(checkResult);
                if(this.opts.errorCallback) this.opts.errorCallback(checkResult, this);
            }
        },
        _preCheck: function(){       //上传之前的预处理
            if(this.opts.precheckCallback){
                var preCheckResult = this.opts.precheckCallback(this.file);
                if(preCheckResult){
                    return preCheckResult;
                }
            }

            if(this.opts.extReg && this.opts.extReg.test(this.file.name)){
                return '不支持上传的类型';
            }
            var maxsize_byte = this._getMaxSize();
            if( this.file.size > maxsize_byte ){
                return '大小不能超过 ' + Math.floor(maxsize_byte / 1024 / 1024) + 'M';
            }
            return '';
        },
        _buildParams: function(){
            var fd = new FormData(),
                params = this.opts.params || {};
            fd.append(this.opts.fileStreamAlias || 'file', this.file);

            for(var name in params) {
                fd.append(name,  params[name]);
            }
            return fd;
        },
        _getMaxSize: function(){
            var maxSize = Number(this.opts.maxsize),
                unit_mb = 1024 * 1024;

            if(maxSize === undefined || isNaN(maxSize)){
                return 10 * unit_mb;
            }

            return maxSize * unit_mb;
        },
        _bindXhrEvent: function(){
            //处理上传进度
            this.xhr.upload.onprogress = function(e) {
                if(this.opts.progressCallback){
                    this.opts.progressCallback(e.loaded / e.total);
                }
            }.bind(this);

            //处理上传成功
            this.xhr.addEventListener('readystatechange', function(response) {
                try{
                    if (this.xhr.readyState == 4) {
                        if(this.xhr.status == 200){
                            var res = JSON.parse(response.target.responseText);
                            if(res.errorMessage){
                                console.error('出错了：' + res.errorMessage);
                                if(this.opts.errorCallback){
                                    this.opts.errorCallback(res.errorMessage, this);
                                }
                            }else{
                                this.opts.successCallback(res, this);
                            }
                        }else{
                            var errMsg = response.target.responseText || '程序出错， 请联系负责人';

                            console.error('出错了：' + errMsg);
                            if(this.opts.errorCallback){
                                this.opts.errorCallback(errMsg, this);
                            }
                        }
                    }
                }catch(e){}
            }.bind(this), false);
        }
    };


    angular.module('dui.component.upload', []).factory('DuiFileUploaderService', function(){

        /*
         * thumbnailConfig:
         * 		attachName: 附件名称
         * 		attachPath: 缩略图url
         * 		domain:	上传到文件服务器并且上传类型为图片的时候，才需要提供
         * 		isJiaoyi: 是否是交易管家， 交易管家的
         */
        function buildThumbnailUrl(thumbnailConfig){

            //照片处理
            if(isImage(thumbnailConfig.attachName)){
                if(thumbnailConfig.domain){		//文件服务器图片
                    var fileServerDomain = 'http://fs.dooioo.' + ((/\.com$/.test(location.hostname || '') ? 'com' : 'org'));

                    return fileServerDomain + '/fetch/vp/' + thumbnailConfig.domain + '/ptgi/' +
                        (thumbnailConfig.isJiaoyi ? '100x100' : '200x150') + '/' + thumbnailConfig.attachPath;
                }else{
                    return thumbnailConfig.attachPath;		//本地服务器图片
                }
            }

            //非照片
            return parseNonImageUrl(thumbnailConfig.attachName, thumbnailConfig.isJiaoyi);
        }

        //判断是否是图片
        function isImage(attachName){
            return (/\.(bmp|gif|jpg|jpeg|png)$/i).test(attachName);
        }

        //解析非图片url
        function parseNonImageUrl(attachName, isJiaoyi){
            var mapping = {
                '\.(xls|xlsx)$': 'excel',
                '\.(doc|docx)$': 'word',
                '\.(pdf)$': 'pdf',
                '\.(txt)$': 'text',
                '\.(wma|wav|mp3)$': 'audio',
                '\.(avi|mov|mpeg|rm)$': 'video'
            };

            for(var reg in mapping){
                if( new RegExp(reg, 'i').test(attachName) ){
                    return buildAttachUrl(mapping[reg]);
                }
            }

            return buildAttachUrl('other');

            function buildAttachUrl(attachType){
                return 'http://dui.dooioo.com/public/images/upload_' + attachType + (isJiaoyi ? '_100x100' : '') + '.png';
            }
        }


        return {
            FileUploader: FileUploader,
            buildThumbnailUrl: buildThumbnailUrl,
            parseNonImageUrl: parseNonImageUrl,
            isImage: isImage
        }
    }).directive('duiDisplayAttachments', function(DuiFileUploaderService){
        /*未列出的接口属性配置
         *
         * 	attachPathAlias: 从返回的附件json对象中， 指定附件路径的key（只有是照片的情况下才需要）
         attachNameAlias: 从返回的附件json对象中， 指定附件名称的key
         domain: 有该属性表示是文件服务器上的附件
         */

        return {
            restrict: 'E',
            replace: true,
            scope: {
                attachList: '=',
                previewCallback: '&'
            },
            template: '<ul class="clearfix" ng-class="{jiaoyiAttachUploader: isJiaoyi, attachUploader: !isJiaoyi}">\
							<li class="thumbViewWrapper" ng-repeat="attach in attachList">\
								<div class="thumbView" ng-click="previewCallback({attach:attach})">\
									<img ng-src="{{buildThumbnailUrl(attach)}}"/>\
								</div>\
								<p class="textDesc mt_5" title="{{attach[attachNameAlias]}}">\
									<a href="javascript:;" ng-click="previewCallback({attach:attach})">{{attach[attachNameAlias]}}</a>\
								</p>\
							</li>\
						</ul>',
            link: function(scope, el, attrs){
                scope.attachPathAlias = attrs.attachPathAlias || 'virtualPath';
                scope.attachNameAlias = attrs.attachNameAlias || 'originalFileName';

                scope.buildThumbnailUrl = function(attach){
                    if(!attach) return;

                    return DuiFileUploaderService.buildThumbnailUrl({
                        attachName: attach[scope.attachNameAlias],
                        attachPath: attach[scope.attachPathAlias],
                        domain: attrs.domain,
                        isJiaoyi: attrs.isJiaoyi !== undefined
                    });
                }
            }
        };
    }).directive('duiUpload', function($http, $timeout, DuiFileUploaderService){
        /*
         模型结构：
         {
         attachPath: //文件路径
         attachName: //元素文件名
         }


         接口参数：
         attachList: 上传的附件列表

         //静态配置
         url: 上传的url地址
         multiple:  是否支持多选文件上传
         maxsize: 上传的最大值，单位为M
         domain:  指定上传的应用（只有在上传文件到文件服务器上时用到）
         fileStreamAlias: 后端接收， 上传文件流的参数名， 默认为file
         attachUrlAlias: 页面显示缩略图的url地址
         attachNameAlias: 页面缩略图显示的文件名
         */

        return {
            restrict: 'E',
            replace: true,
            scope: {
                attachList: '=',
                getParams: '&',
                successCallback: '&',
                previewCallback: '&',
                deleteCallback: '&',
                precheckCallback: '&'		//预检查回调函数
            },
            template: '<div>\
						<button type="button" class="btn-small btn-green in_block right mr_10 js_angular_fakeupload" ng-class="{disable: cannotUpload}" ng-disabled="cannotUpload">导入</button>\
						<input type="file" class="hideit js_angular_trueUpload"></input>\
						<ul class="clearfix" ng-class="{jiaoyiAttachUploader: isJiaoyi, attachUploader: !isJiaoyi}" ng-show="false">\
							<li class="thumbViewWrapper" id="attach{{attach.identity}}" ng-repeat="attach in attachList">\
								<div class="thumbView">\
									<a href="javascript:;" class="del" ng-click="deleteAttach(attach, $index, $event)"></a>\
									<img dui-if="!attach.errMsg && !attach.uploading" ng-src="{{buildThumbnailUrl(attach)}}" ng-click="previewCallback({attach:attach})"/>\
									<p class="errMsg" title="{{attach.errMsg}}">{{attach.errMsg}}</p>\
								</div>\
								<p class="textDesc mt_5" title="{{attach[attachNameAlias]}}">\
									<span ng-show="attach.uploading || attach.errMsg">{{attach[attachNameAlias]}}</span>\
									<a ng-show="!attach.uploading && !attach.errMsg" href="javascript:;" ng-click="previewCallback({attach:attach})">{{attach[attachNameAlias]}}</a>\
								</p>\
								<div class="bar js_bar" ng-hide="attach.errMsg" ng-show="attach.uploading"><div class="barFront js_barFront"></div></div>\
								<div class="mask" ng-show="attach.uploading"></div>\
							</li>\
						</ul>\
					</div>',
            link: function(scope, el, attrs){
                var trueUploader = el[0].querySelector('.js_angular_trueUpload'),
                    fakeUploader = el[0].querySelector('.js_angular_fakeupload'),
                    fileServerDomain = 'http://fs.dooioo.' + ((/\.com$/.test(location.hostname || '') ? 'com' : 'org')),
                    uploadUrl = attrs.domain ? fileServerDomain + '/store' : attrs.url;

                scope.fileStreamAlias = attrs.fileStreamAlias || 'file';
                scope.attachNameAlias = attrs.attachNameAlias || 'originalFileName';
                scope.attachPathAlias = attrs.attachPathAlias || 'virtualPath';

                scope.precheckCallback = scope.precheckCallback || function() { return ''; };

                //判断是否是交易管家系统
                scope.isJiaoyi = attrs.appName === 'jiaoyi';

                scope.getParams = scope.getParams || function() { return {}; };

                //点击上传按钮，触发系统的选择文件对话框
                fakeUploader.addEventListener('click', function(){
                    trueUploader.click();
                }, false);

                //选择文件，对上传进行上传
                trueUploader.addEventListener('change', function(){
                    var files = this.files;

                    for(var i=0, len=files.length; i<len; i++){
                        var identity = generateId(i);

                        //构建上传前附件model
                        scope.attachList.push(buildBeforeUploadItem(files[i], identity));

                        //开始上传附件
                        new DuiFileUploaderService.FileUploader(files[i], uploadUrl, buildUploadConfig(files[i], identity));
                    }

                    scope.$apply();

                    this.value = '';
                }, false);


                //删除附件
                scope.deleteAttach = function(attach, idx, $event){
                    $event.stopPropagation();

                    if(confirm('确定删除')){
                        if(attrs.deleteCallback){		//异步删除
                            var result = scope.deleteCallback({attach: attach});
                            if(!result) return;

                            if(result === true){
                                scope.attachList.splice(idx, 1);
                                return;
                            }

                            if(result.success){
                                result.success(function(res){
                                    scope.attachList.splice(idx, 1);
                                }).error(function(err){
                                    console.log(err);
                                });
                            }
                        }else{
                            scope.attachList.splice(idx, 1);
                        }
                    }
                };

                //构建缩略图完整的url
                scope.buildThumbnailUrl = function(attach){
                    if(!attach) return;

                    return DuiFileUploaderService.buildThumbnailUrl({
                        attachName: attach[scope.attachNameAlias],
                        attachPath: attach[scope.attachPathAlias],
                        domain: attrs.domain,
                        isJiaoyi: attrs.isJiaoyi !== undefined
                    });
                };

                //设置多选
                if(attrs.multiple === 'true'){
                    trueUploader.setAttribute('multiple', true);
                }


                //控制上传文件的个数
                if(attrs.maxUploadCount !== undefined && isNaN(attrs.maxUploadCount) === false){
                    trueUploader.removeAttribute('multiple');

                    scope.$watch('attachList', function(attachList){
                        if(!attachList) return;

                        var alreadyUploaded = attachList.filter(function(attach){
                            return !attach.uploading && !attach.errMsg;			//已经上传完成 && 没有错误消息， 就表示已经有有效的上传文件了
                        });

                        scope.cannotUpload = (alreadyUploaded.length >= Number(attrs.maxUploadCount));
                    }, true);
                }

                //显示前构建本地数据模型
                function buildBeforeUploadItem(file, identity){
                    var attachInfo = {};

                    attachInfo.identity = identity;
                    attachInfo.uploading = true;
                    attachInfo[scope.attachNameAlias] = file.name;

                    return attachInfo;
                }

                //构建上传文件参数配置
                function buildUploadConfig(file, identity){
                    var uploadConfig = {
                        params: buildReqParams(file),
                        fileStreamAlias: scope.fileStreamAlias,
                        progressCallback: function(progress){	//进度条事件
                            document.getElementById('attach' + identity).querySelector('.js_barFront').style.width = Math.ceil((scope.isJiaoyi ? 90 : 130) * progress) + 'px';
                        },
                        precheckCallback: function(file){
                            return scope.precheckCallback({file: file});
                        },
                        successCallback: function(res){
                            if(attrs.domain && attrs.singleSubmit === undefined){	//上传到公共服务器，二次提交
                                $http({url: attrs.url, method: 'POST', data: jsonToQueryString(angular.extend(res, scope.getParams())),
                                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function(dbRes){	//数据入库
                                    successCallback(dbRes, identity);
                                }).error(function(errMsg){
                                    config.errorCallback({errMsg: errMsg});
                                });
                            }else{				//上传到本地服务器
                                successCallback(res, identity);
                            }

                            $timeout(function() { scope.$apply(); });
                        },
                        errorCallback: function(errMsg){
                            var idx = findAttachByIdentity(identity);
                            if(idx !== -1){
                                scope.attachList[idx].errMsg = errMsg;
                                scope.attachList[idx].uploading = false;
                            }
                            $timeout(function() { scope.$apply(); });
                        }
                    };

                    copyProperty(attrs, uploadConfig, 'maxsize');

                    return uploadConfig;
                }

                //构建请求参数
                function buildReqParams(file){
                    var result = {};

                    //配置（上传到文件服务器相关的）静态参数
                    copyProperty(attrs, result, 'category');
                    copyProperty(attrs, result, 'domain');

                    //上传到文件服务器，但在指令调用时没有配置category的情况
                    if(attrs.domain && attrs.category === undefined){
                        var category = 'OTHER';
                        if(/^image.*$/.test(file.type)){
                            category = 'IMAGE';
                        }else if(/^audio.*$/.test(file.type)){
                            category = 'AUDIO';
                        }else if(/^video.*$/.test(file.type)){
                            category = 'VIDEO';
                        }
                        result.category = category;
                    }

                    //加上额外的参数
                    return angular.extend(result, scope.getParams());
                }

                //上传成功回调
                function successCallback(res, identity){
                    var idx = findAttachByIdentity(identity);
                    if(idx !== -1){
                        scope.attachList[idx] = res;
                    }
                    if(scope.successCallback){
                        scope.successCallback({attach: res});
                    }
                }


                //根据标识identity找到附件对象
                function findAttachByIdentity(identity){
                    var list = scope.attachList;
                    for(var i=0, len=list.length; i<len; i++){
                        if(list[i].identity === identity){
                            return i;
                        }
                    }
                    return -1;
                }

                //从fromObj中指定的属性拷贝到toObj中
                function copyProperty(fromObj, toObj, fieldName){
                    if(fromObj[fieldName]){
                        toObj[fieldName] = fromObj[fieldName];
                    }
                }

                //json对象转成查询字符串： {empNo: '12345', empName: '张三'} ==> empNo=12345&empName=张三
                function jsonToQueryString(json){
                    var result = [];
                    for(var key in json){
                        if(json[key]){
                            result.push(key + '=' + json[key]);
                        }
                    }
                    return result.join('&');
                }

                //生成随机id，上传成功后的model替换，上传前的本地model
                function generateId(idx){
                    return Number(new Date()) + '_' + Math.ceil(Math.random() * 1000000) + '_' + idx;
                }
            }
        };
    });
}());