'use strict';

//  Martialarts Tuition Controller
catwalkApp.controller('MartialartsTuitionController', ['$scope','$location','$stateParams','$global.services', 'MartialartsTuition',
    function ($scope,location,$stateParams,$services, service) {
        $scope.name = "Tuition";
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
            location.path('/martialarts/tuition/');
        };

        $scope.update= function(id){
            location.path('/martialarts/tuition/' + id);
        };

        $scope.back = function () {
            window.history.back();
        };

        if($stateParams.id){ $scope.get($stateParams.id);}
        else{ $scope.list();}
    }
]);

//  Martialarts Tuition Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider, USER_ROLES) {
        $stateProvider
        .state('martialarts.tuition', {
            url: "/tuition",
            templateUrl: "components/model/tuition/tuitionTable.html",
            controller: 'MartialartsTuitionController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
        .state('martialarts.tuitionForm', {
            url: "/tuition/:id",
            templateUrl: "components/model/tuition/tuitionForm.html",
            controller: 'MartialartsTuitionController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
     }
]);
