/**
 *   Routing
 */
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {
        $stateProvider
            .state('admin', {
                abstract: false,
                url: "/admin",
                templateUrl: "components/admin/layout/layout.html",
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            });
    }
]).run(securityHandler);

