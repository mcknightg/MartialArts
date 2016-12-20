'use strict';

//  Martialarts Attendance Controller
catwalkApp.controller('MartialartsAttendanceController', ['$scope','$location','$stateParams','$global.services', 'MartialartsAttendance',
    function ($scope,location,$stateParams,$services, service) {
        $scope.name = "Attendance";
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
            location.path('/martialarts/attendance/');
        };

        $scope.update= function(id){
            location.path('/martialarts/attendance/' + id);
        };

        $scope.back = function () {
            window.history.back();
        };

        if($stateParams.id){ $scope.get($stateParams.id);}
        else{ $scope.list();}
    }
]);

//  Martialarts Attendance Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider, USER_ROLES) {
        $stateProvider
        .state('martialarts.attendance', {
            url: "/attendance",
            templateUrl: "components/model/attendance/attendanceTable.html",
            controller: 'MartialartsAttendanceController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
        .state('martialarts.attendanceForm', {
            url: "/attendance/:id",
            templateUrl: "components/model/attendance/attendanceForm.html",
            controller: 'MartialartsAttendanceController',
            access: {
                authorizedRoles: [USER_ROLES.all]
            }
        })
     }
]);
