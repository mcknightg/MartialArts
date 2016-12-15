catwalkApp.controller('ServicesDocController', ['$scope','$global.services','$http',
    function ($scope,$services,$http) {
        $scope.urls = {};
        $scope.service = '';
        $scope.schema = {};
        $scope.servicename = '';

        $scope.update = function(){
            var mod = $scope.urls[$scope.service]['mod'];
            var name = $scope.urls[$scope.service]['name'];
            $scope.servicename = $scope.camelCase(mod) + $scope.camelCase(name);
            $http.get($scope.urls[$scope.service]['schema']).
            success(function(data) {
                $scope.schema = data;
            });
        };

        $scope.camelCase = function(text){
            var nameArray = text.split("_");
            var retText = "";
            angular.forEach(nameArray, function(value, key) {
                retText +=  value.charAt(0).toUpperCase() + value.substr(1);
            });
            return retText;
        };
        $scope.getServiceNames = function(){
            $services.api.get(function(apiData){
                angular.forEach(apiData['mods'], function(mod, key) {
                    angular.forEach(mod, function(model, key) {
                        $scope.urls[key] = model;
                    });
                });
            });
        };
        $scope.getServiceNames();
    }
]);
// Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {
        $stateProvider
            .state('admin.docs.services', {
                url: "/services",
                templateUrl: "components/admin/docs/services/services.html",
                controller: "ServicesDocController",
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
    }
]);