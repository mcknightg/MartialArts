catwalkApp.controller('LacetController', ['$scope','$global.services','$stateParams','Account',
    function ($scope,$services,$stateParams,Account) {
        $scope.id = $stateParams.id;
        $scope.page = $stateParams.page;
        $scope.account_service = Account;
    }
]);

catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {
        $urlRouterProvider.otherwise("/lacet/Home");
        $stateProvider
            .state('laceteditor', {
                url: "/laceteditor",
                templateUrl: "components/admin/lacet/editor.html",
                controller: "LacetController",
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .state('lacetviewer', {
                url: "/lacetviewer",
                templateUrl: "components/admin/lacet/viewer.html",
                controller: "LacetController"
            })
            .state('lacet', {
                url: "/lacet/:page",
                templateUrl: "components/admin/lacet/viewer.html",
                controller: "LacetController"
            })
            .state('lacetitem', {
                url: "/lacet/:page/:id",
                templateUrl: "components/admin/lacet/viewer.html",
                controller: "LacetController"
            })
    }
]);
catwalkApp.directive("lacetEditor", angularLacetEditor );
catwalkApp.directive("lacetViewer", angularLacetViewer );