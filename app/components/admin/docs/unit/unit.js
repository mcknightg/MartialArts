catwalkApp.controller('UnitTestController', ['$scope','$global.services',
    function ($scope,$services) {
        $scope.results = {};
        $scope.runUnitTests =function(){
            var user = {"firstName": "John", "lastName": "Doe", "password": "dow", "langKey": "en", "tenantId": "BluntSoftware", "activationKey": "2342353534235", "login": "johnd", "email": "johnd@gmail.com", "activated": "true"};
            $scope.insertID = null;
            $scope.validSchema();
            $scope.validColumns();
            $scope.insertUser(user,function(user){
                user.firstName = "Fred";
                user.lastName = "Henry";
                user['unknownField'] = "Should Not Fail";
                $scope.editUser(user,function(user){
                    $scope.getUser(user,function(user){
                        $scope.removeUser(user);
                    });
                });
            });
            /*var queryParams = {filterByFields:{'name':'Fred'},sidx:'id',rows:500,page:1,defaultsearchoper:"icn"};
             $services.UserManagerApplicationUser.query(queryParams,function(json){

             });*/

        };
        $scope.getUser = function(user,cb){
            $services.UserManagerApplicationUser.get(user,function(json){
                if(json && $scope.insertID === json.id) {
                    $scope.results['get'] = "Success" ;
                }else{
                    $scope.results['get'] = "Failed" ;
                }
                if(cb){cb(json);}
            },function(error){
                $scope.results['get'] = error ;
            });
        };
        $scope.removeUser = function(user,cb){
            $services.UserManagerApplicationUser.delete(user,function(json){

                if(json && $scope.insertID === json.id) {
                    $scope.results['delete'] = "Success" ;
                }else{
                    $scope.results['delete'] = "Failed" ;
                }
                if(cb){cb(json);}
            },function(error){
                $scope.results['delete'] = error ;
            });
        };
        $scope.editUser = function(user,cb){
            $services.UserManagerApplicationUser.save(user,function(json){
                if($scope.insertID === json.id) {
                    $scope.results['edit'] = "Success" ;
                }else{
                    $scope.results['edit'] = "Failed" ;
                }
                if(cb){cb(json);}
            },function(error){
                $scope.results['edit'] = error ;
            });
        };
        $scope.insertUser = function(user,cb){
            $services.UserManagerApplicationUser.save(user,function(json){
                $scope.insertID = json.id;
                if($scope.insertID) {
                    $scope.results['insert'] = "Success" ;
                }else{
                    $scope.results['insert'] = "Failed" ;
                }
                if(cb){cb(angular.copy(json));}
            },function(error){
                $scope.results['insert'] = error ;
            });
        };
        $scope.validColumns = function(){
            $services.UserManagerApplicationUser.columns(function(json){
                // $scope.results['columnsData'] = json;
                var success = json.columns;
                if(success) {
                    $scope.results['columns'] = "Success" ;
                }else{
                    $scope.results['columns'] = "Failed" ;
                }
            },function(error){
                $scope.results['columns'] = error ;
            });
        };
        $scope.validSchema = function(){
            $services.UserManagerApplicationUser.schema(function(json){
                //$scope.results['schemaData'] = json;
                var success = json.firstName === "";
                if(success) {
                    $scope.results['schema'] = "Success" ;
                }else{
                    $scope.results['schema'] = "Failed" ;
                }
            },function(error){
                $scope.results['schema'] = error ;
            });
        }
    } ]);
// Routing
catwalkApp.config(['$stateProvider', '$urlRouterProvider', 'USER_ROLES',
    function ($stateProvider, $urlRouterProvider,USER_ROLES) {
        $stateProvider
            .state('admin.docs.test', {
                url: "/test",
                templateUrl: "components/admin/docs/unit/unit_test.html",
                controller: "UnitTestController",
                access: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            })
    }
]);


   