'use strict';

//  Martialarts Karateclassexception Controller
catwalkApp.controller('MartialartsKarateclassexceptionController', ['$scope','$location','$stateParams','$global.services', 'MartialartsKarateclassexception',
    function ($scope,location,$stateParams,$services, service) {
        $scope.name = "Karateclassexception";
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
            location.path('/martialarts/karateclassexception/');
        };

        $scope.update= function(id){
            location.path('/martialarts/karateclassexception/' + id);
        };

        $scope.back = function () {
            window.history.back();
        };

        if($stateParams.id){ $scope.get($stateParams.id);}
        else{ $scope.list();}
    }
]);

//  Martialarts Karateclassexception Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider, USER_ROLES) {
        $stateProvider
        .state('martialarts.karateclassexception', {
            url: "/karateclassexception",
            templateUrl: "components/model/karateclassexception/karateclassexceptionTable.html",
            controller: 'MartialartsKarateclassexceptionController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
        .state('martialarts.karateclassexceptionForm', {
            url: "/karateclassexception/:id",
            templateUrl: "components/model/karateclassexception/karateclassexceptionForm.html",
            controller: 'MartialartsKarateclassexceptionController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
     }
]);
