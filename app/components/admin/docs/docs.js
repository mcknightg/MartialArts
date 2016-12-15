catwalkApp.controller('DocsController', ['$scope','$global.services',
    function ($scope,$services) {
       
    }
]);

//  Home Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {
        $stateProvider
            .state('admin.docs', {
                url: "/docs",
                templateUrl: "components/admin/docs/docs.html",
                controller: "DocsController",
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
            .state('admin.docs.intro', {
                url: "/intro",
                templateUrl: "components/admin/docs/intro.html",
                controller: "DocsController",
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })

             
    }
]);
