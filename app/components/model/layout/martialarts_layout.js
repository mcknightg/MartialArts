

catwalkApp.config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
        $stateProvider
        .state('martialarts', {
            abstract: false,
            url: "/martialarts",
            templateUrl: "components/model/layout/martialarts_layout.html"
        })

     }
]);
