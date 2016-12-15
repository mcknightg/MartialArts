'use strict';

//  Martialarts Student Controller
catwalkApp.controller('MartialartsStudentController', ['$scope','$location','$stateParams','$global.services', 'MartialartsStudent',
    function ($scope,location,$stateParams,$services, service) {
        $scope.name = "Student";
        $scope.listParams = {rows:12,page:1,defaultsearchoper:"icn"};
        $scope.srchterm = '';

        $scope.get = function(id){
            $scope.modelData = service.get({id: id});
        };
        $scope.setPage = function(page){
            $scope.listParams.page = page;
            $scope.list();
        };
        $scope.search = function(){
            if($scope.srchterm && $scope.srchterm !== '' ){
                $scope.listParams['filterByFields'] =  {'name':$scope.srchterm};
            }else{
                $scope.listParams['filterByFields'] = {};
            }
            $scope.list();
        };
        $scope.save = function(){
            service.save($scope.modelData,function(){
                  $scope.back();
            });
        };

        $scope.list = function(){
            $scope.modelList = service.query($scope.listParams);
        };

        $scope.remove = function(id){
            service.delete({id: id}, function () {

            });
        };

        $scope.new= function(){
            location.path('/martialarts/student/');
        };

        $scope.update= function(id){
            location.path('/martialarts/student/' + id);
        };

        $scope.back = function () {
            window.history.back();
        };

        if($stateParams.id){ $scope.get($stateParams.id);}
        else{ $scope.list();}
    }
]);

//  Martialarts Student Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider, USER_ROLES) {
        $stateProvider
        .state('martialarts.student', {
            url: "/student",
            templateUrl: "components/model/student/studentTable.html",
            controller: 'MartialartsStudentController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
        .state('martialarts.studentForm', {
            url: "/student/:id",
            templateUrl: "components/model/student/studentForm.html",
            controller: 'MartialartsStudentController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
     }
]);
