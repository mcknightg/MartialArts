'use strict';

//  UserManager ApplicationUser Controller
catwalkApp.controller('UserController', ['$scope','$location','$stateParams','$global.services', 'UserManagerApplicationUser',
    function ($scope,location,$stateParams,$services, service) {
        $scope.name = "ApplicationUser";
        $scope.listParams = {rows:12,page:1,defaultsearchoper:"icn"};
        $scope.srchterm = '';

        $scope.addRole = function(){
            if($scope.roleIdToAdd && $scope.modelData.id){
                $services.UserManagerApplicationUserAuthority.save({appuser:$scope.modelData.id,authority:$scope.roleIdToAdd},function(){
                    $scope.getUserRoles();
                });
            }
        };
        $scope.removeRole = function(id){
            if(id){
                $services.UserManagerApplicationUserAuthority.delete({id:id},function(){
                    $scope.getUserRoles();
                });
            }
        };
        $scope.getUserRoles = function(){
            $services.UserManagerApplicationUserAuthority.query({filterByFields:{'appuser.id':$scope.modelData.id},rows:12,page:1,defaultsearchoper:"icn"},function(userRoles){
                $scope.userRoles = userRoles.rows;
            });
        };

        $services.UserManagerApplicationAuthority.query({rows:12,page:1,defaultsearchoper:"icn"},function(roles){
            $scope.roles = roles;
        });


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
        $scope.onFilesSelected=function(files){
            $scope.file = files[0].File;

        };

        $scope.get = function(id){

            service.get({id: id},function(user) {
                $scope.modelData = user;
                $scope.imageSrc = "";
                if(user.imgSrc){
                    $scope.imageSrc =user.imgSrc;
                }
                $scope.getUserRoles();
            });

        };

        $scope.save = function(){
            $scope.modelData['imgSrc'] = $scope.imageSrc;
            service.save($scope.modelData,function(){
                $scope.back();
            });
        };
        $scope.setPage = function(page){
            $scope.listParams.page = page;
            $scope.list();
        };
        $scope.search = function(){
            if($scope.srchterm && $scope.srchterm !== '' ){
                $scope.listParams['filterByFields'] =  {'login':$scope.srchterm};
            }else{
                $scope.listParams['filterByFields'] = {};
            }
            $scope.list();
        };
        $scope.list = function(){

            $scope.modelList = service.query($scope.listParams,function(){
                console.log($scope.modelList);
            });
        };

        $scope.remove = function(id){
            service.delete({id: id}, function () {
                $scope.list();
            });
        };

        $scope.new= function(){
            location.path('/admin/admin/user/');
        };

        $scope.update= function(id){
            location.path('/admin/admin/user/' + id);
        };

        $scope.back = function () {
            window.history.back();
        };

        if($stateParams.id){ $scope.get($stateParams.id);}
        else{ $scope.list();}
    }
]);

//  UserManager ApplicationUser Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {
        $stateProvider
            .state('admin.users', {
                url: "/admin/users",
                templateUrl: "components/admin/users/users.html",
                controller: 'UserController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .state('admin.user', {
                url: "/admin/user/:id",
                templateUrl: "components/admin/users/userform.html",
                controller: 'UserController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
    }
]);
