catwalkApp.controller('ActivationController', function ($scope, $routeParams, Activate) {
    Activate.get({key: $routeParams.key},
        function (value, responseHeaders) {
            $scope.error = null;
            $scope.success = 'OK';
        },
        function (httpResponse) {
            $scope.success = null;
            $scope.error = "ERROR";
        });
});
catwalkApp.controller('ErrorController',function ($scope) {
    $scope.error = "You are not authorized to access this page.";
});    
catwalkApp.controller('ResetPasswordController',function ($scope, $translate, ResetPassword,ChangePassword,$stateParams) {
    $scope.success = null;
    $scope.error = null;
    if($stateParams.token){ 
        $scope.token =  $stateParams.token;
        //alert("Rest Password for Token " + $stateParams.token);
    }
    $scope.changePassword =function () {
        ChangePassword.get({'password':$scope.password,'token':$scope.token},
            function (value, responseHeaders) {
                $scope.error = null;
                $scope.errorUserExists = null;
                $scope.success = 'OK';
            },
            function (httpResponse) {
                $scope.success = null;
                if (httpResponse.status === 304 &&
                    httpResponse.data.error && httpResponse.data.error === "Not Modified") {
                    $scope.error = null;
                    $scope.errorUserExists = "ERROR";
                } else {
                    $scope.error = "ERROR";
                    $scope.errorUserExists = null;
                }
            });
    };
    $scope.resetPassword = function () {

        ResetPassword.get({'email':$scope.accountemail},
            function (value, responseHeaders) {
                $scope.error = null;
                $scope.errorUserExists = null;
                $scope.success = 'OK';
            },
            function (httpResponse) {
                $scope.success = null;
                if (httpResponse.status === 304 &&
                    httpResponse.data.error && httpResponse.data.error === "Not Modified") {
                    $scope.error = null;
                    $scope.errorUserExists = "ERROR";
                } else {
                    $scope.error = "ERROR";
                    $scope.errorUserExists = null;
                }
            });
    }
});
catwalkApp.controller('RegisterController',function ($scope, $translate, Register) {
    $scope.success = null;
    $scope.error = null;
    $scope.doNotMatch = null;
    $scope.errorUserExists = null;
    $scope.registerAccount = {};
    $scope.confirmPassword ="";
    $scope.register = function () {
       console.log("Registering User");
        console.log($scope.registerAccount.password + " -- " + $scope.registerAccount.confirmPassword);
        if ($scope.registerAccount.password != $scope.registerAccount.confirmPassword) {
            $scope.doNotMatch = "ERROR";
        } else {
            $scope.registerAccount.langKey = $translate.use();
            $scope.doNotMatch = null;
            Register.save($scope.registerAccount,
                function (value, responseHeaders) {
                    $scope.error = null;
                    $scope.errorUserExists = null;
                    $scope.success = 'OK';
                },
                function (httpResponse) {
                    $scope.success = null;
                    if (httpResponse.status === 304 &&
                        httpResponse.data.error && httpResponse.data.error === "Not Modified") {
                        $scope.error = null;
                        $scope.errorUserExists = "ERROR";
                    } else {
                        $scope.error = "ERROR";
                        $scope.errorUserExists = null;
                    }
                });
        }
    }
});
catwalkApp.controller('LoginController', ['$scope', '$rootScope', '$location','AuthenticationSharedService',
    function ($scope, $rootScope,$location, AuthenticationSharedService) {
        $scope.rememberMe = true;
        $scope.login = function () {
            AuthenticationSharedService.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe,
                success:function(data){

                }
            })
        };
        $scope.loginFacebook = function () {
            window.location = base_url + 'auth/facebook?scope=email';
        };
        $scope.loginLinkedIn = function () {
            window.location = base_url + 'auth/linkedin';
        };
        $scope.loginGoogle = function () {
            window.location = base_url + 'auth/google?scope=email';
        };
        $scope.loginGithub = function () {
            window.location = base_url + 'auth/github';
        };
        $scope.loginTwitter = function () {
            window.location = base_url + 'auth/twitter';
        };
    }]);

catwalkApp.controller('LogoutController', function ($location, AuthenticationSharedService) {
    AuthenticationSharedService.logout();
});
catwalkApp.controller('SettingsController', ['$scope' ,'Account',
    function ($scope,Account) {
        $scope.success = null;
        $scope.error = null;

        $scope.get = function(){
            return Account.get(function(data){
                $scope.imageSrc = "";
                if(data.imgSrc){
                    console.log(data.imgSrc);
                    $scope.imageSrc = data.imgSrc;
                }
            });
        };
        $scope.settingsAccount = $scope.get();
        $scope.onFilesSelected=function(files){
            $scope.file = files[0].File;

        }; 


        $scope.rotateImage=function(){
            var image = new Image();
            image.src = $scope.imageSrc;
            var canvas = document.createElement("canvas");
            canvas.width = image.height;
            canvas.height = image.width;
            var ctx = canvas.getContext("2d");
            ctx.clearRect(0,0,canvas.width,canvas.height);
            ctx.save();
            ctx.translate(canvas.width/2,canvas.height/2);
            ctx.rotate(90 * Math.PI / 180);
            ctx.drawImage(image,-image.width/2,-image.height/2);
            ctx.restore();
            $scope.imageSrc = canvas.toDataURL("image/jpeg");
        };

        $scope.save = function () {
            $scope.settingsAccount['imgSrc'] = $scope.imageSrc;
            Account.save($scope.settingsAccount,
                function (value, responseHeaders) {
                    $scope.error = null;
                    $scope.success = 'OK';
                    $scope.settingsAccount = $scope.get();
                    $scope.log("Success");

                },
                function (httpResponse) {
                    $scope.success = null;
                    $scope.error = "ERROR";
                    $scope.log(httpResponse);
                });
        };
    }]);

catwalkApp.controller('PasswordController', ['$scope', 'Password',
    function ($scope, Password) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.changePassword = function () {
            if ($scope.password != $scope.confirmPassword) {
                $scope.doNotMatch = "ERROR";
            } else {
                $scope.doNotMatch = null;
                Password.save({'password':$scope.password},
                    function (value, responseHeaders) {
                        $scope.error = null;
                        $scope.success = 'OK';
                    },
                    function (httpResponse) {
                        $scope.success = null;
                        $scope.error = "ERROR";
                    });
            }
        };
    }]);

catwalkApp.controller('SessionsController', ['$scope', 'resolvedSessions', 'Sessions',
    function ($scope, resolvedSessions, Sessions) {
        $scope.success = null;
        $scope.error = null;
        $scope.sessions = resolvedSessions;
        $scope.invalidate = function (series) {
            Sessions.delete({series: encodeURIComponent(series)},
                function (value, responseHeaders) {
                    $scope.error = null;
                    $scope.success = "OK";
                    $scope.sessions = Sessions.get();
                },
                function (httpResponse) {
                    $scope.success = null;
                    $scope.error = "ERROR";
                });
        };
    }]);


