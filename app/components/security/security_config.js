catwalkApp.config(['$stateProvider', '$urlRouterProvider', '$translateProvider','USER_ROLES',
    function ($stateProvider, $urlRouterProvider,$translateProvider,USER_ROLES) {
        $stateProvider
            .state('register', {
                url: "/register",
                templateUrl: 'components/security/register.html',
                controller: 'RegisterController'
            }) 
            .state('resetpassword', {
                url: "/resetpassword",
                templateUrl: 'components/security/reset_password.html',
                controller: 'ResetPasswordController'
            })
            .state('resetpasswordtoken', {
                url: "/resetpassword/:token",
                templateUrl: 'components/security/reset.html',
                controller: 'ResetPasswordController'
            })
            .state('login', {
                url: "/login",
                templateUrl: "components/security/login.html",
                controller: 'LoginController'
            })
            .state('error', {
                url: "/error",
                templateUrl: "components/security/error.html",
                controller: 'ErrorController'
            })
            .state('logout', {
                url: "/logout",
                templateUrl: 'components/home/home.html',
                controller: 'LogoutController',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .state('index.settings',  {
                url: "/settings",
                templateUrl: 'components/security/settings.html',
                controller: 'SettingsController',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .state('index.sessions',  {
                url: "/sessions",
                templateUrl: 'components/security/sessions.html',
                controller: 'SessionsController',
                resolve:{
                    resolvedSessions:['Sessions', function (Sessions) {
                        return Sessions.get();
                    }]
                },
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .state('index.password', {
                url: "/password",
                templateUrl: 'components/security/password.html',
                controller: 'PasswordController',
                access: {
                    authorizedRoles: [USER_ROLES.all]
                }
            });
        // Initialize angular-translate
        $translateProvider.useStaticFilesLoader({
            prefix: 'i18n/',
            suffix: '.json'
        });
        $translateProvider.preferredLanguage('en');
        $translateProvider.useCookieStorage();
    }
]);