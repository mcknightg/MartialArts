/**
 * INSPINIA - Responsive Admin Theme
 * Copyright 2015 Webapplayers.com
 *
 */

/**
 * MainCtrl - controller
 */

//Main Controller
catwalkApp.controller('DashboardCtrl', ['$scope','$global.services',
    function ($scope,$services) {
        $scope.userName = 'Example user';
        $scope.helloText = 'Welcome To Your New Project';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment.';
    }
]);


//  Home Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {

        $stateProvider
            .state('index.dashboard', {
                url: "/dashboard",
                templateUrl: "components/dashboard/dashboard.html",
                controller: 'DashboardCtrl',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
    }
]);