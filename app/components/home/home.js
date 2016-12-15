/**
 * MainCtrl - controller
 */
catwalkApp.controller('MainCtrl', ['$scope','$global.services','$state','$translate','$window',
    function ($scope,$services,$state,$translate,$window) {
        $translate('company.email').then(function (email) {
            $scope.email = email;
        });
        $translate('company.social.twitter').then(function (twitter) {
            $scope.twitter = twitter;
        });
        $translate('company.social.facebook').then(function (facebook) {
            $scope.facebook = facebook;
        });
        $translate('company.social.linkedin').then(function (linkedin) {
            $scope.linkedin = linkedin;
        });

        var theme = $window.localStorage.getItem('theme') ;
        if(!theme){theme = 'default'}
        
        var themes = {
            "default": "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/flatly/bootstrap.min.css",
            "cerulean" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/cerulean/bootstrap.min.css",
            "cosmo" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/cosmo/bootstrap.min.css",
            "cyborg" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/cyborg/bootstrap.min.css",
            "darkly" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/darkly/bootstrap.min.css",
            "flatly" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/flatly/bootstrap.min.css",
            "journal" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/journal/bootstrap.min.css",
            "lumen" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/lumen/bootstrap.min.css",
            "paper" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/paper/bootstrap.min.css",
            "readable" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/readable/bootstrap.min.css",
            "sandstone" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/sandstone/bootstrap.min.css",
            "simplex" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/simplex/bootstrap.min.css",
            "slate" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/slate/bootstrap.min.css",
            "spacelab" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/spacelab/bootstrap.min.css",
            "superhero" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/superhero/bootstrap.min.css",
            "united" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/united/bootstrap.min.css",
            "yeti" : "https://maxcdn.bootstrapcdn.com/bootswatch/3.3.6/yeti/bootstrap.min.css"
        };
        $scope.themesheet = $('#theme');
        $scope.themesheet.attr('href',themes[theme]);
        $scope.changeTheme = function(theme){
            $window.localStorage.setItem('theme',theme) ;
            $scope.themesheet.attr('href',themes[theme]);
        };

    }
]);
//  Home Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {
        
        $stateProvider
            .state('home', {
                url: "/home",
                templateUrl: "components/home/home.html",
                controller: 'MainCtrl'
            })
            .state('terms_of_service', {
                url: "/terms",
                templateUrl: "components/home/terms_of_service.html",
                controller: 'MainCtrl'
            })
            .state('privacy_policy', {
                url: "/privacy",
                templateUrl: "components/home/privacy_policy.html",
                controller: 'MainCtrl'
            })
    }
]);
