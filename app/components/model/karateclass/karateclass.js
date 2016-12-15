'use strict';

//  Martialarts Karateclass Controller
catwalkApp.controller('MartialartsKarateclassController', ['$scope','$location','$stateParams','$global.services', 'MartialartsKarateclass',
    function ($scope,location,$stateParams,$services, service) {
        $scope.name = "Karateclass";
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
            location.path('/martialarts/karateclass/');
        };

        $scope.update= function(id){
            location.path('/martialarts/karateclass/' + id);
        };

        $scope.back = function () {
            window.history.back();
        };

        if($stateParams.id){ $scope.get($stateParams.id);}
        else{ $scope.list();}
    }
]);

//  Martialarts Karateclass Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider, USER_ROLES) {
        $stateProvider
        .state('martialarts.karateclass', {
            url: "/karateclass",
            templateUrl: "components/model/karateclass/karateclassTable.html",
            controller: 'MartialartsKarateclassController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
        .state('martialarts.karateclassForm', {
            url: "/karateclass/:id",
            templateUrl: "components/model/karateclass/karateclassForm.html",
            controller: 'MartialartsKarateclassController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
     }
]);
