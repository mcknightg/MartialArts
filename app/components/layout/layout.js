//Main Controller
catwalkApp.controller('layoutCtrl', ['$scope','Account','$window',
    function ($scope,Account,$window) {

        Account.get(function(data){
            if(data.theme){
                $scope.changeTheme(data.theme);
            }
        });
    }
]);/**
 *   Routing
 */
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {
        
        $stateProvider
            .state('index', {
                abstract: true,
                url: "/index",
                templateUrl: "components/layout/layout.html",
                controller:'layoutCtrl'
            });
    }
]).run(securityHandler);
