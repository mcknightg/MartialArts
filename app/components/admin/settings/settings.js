'use strict';

catwalkApp.controller('PropertySettingsController', ['$scope','$location','$stateParams','$global.services', 'Properties',
    function ($scope,location,$stateParams,$services, properties) {
          $scope.props = properties.get();
          $scope.save = function(){

          }
    }
]);

//  UserManager ApplicationUser Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {
        $stateProvider
            .state('admin.settings', {
                url: "/admin/settings",
                templateUrl: "components/admin/settings/settings.html",
                controller: 'PropertySettingsController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .state('admin.setting', {
                url: "/admin/settings/:id",
                templateUrl: "components/admin/settings/settingsform.html",
                controller: 'PropertySettingsController',
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
    }
]);