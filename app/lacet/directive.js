var angularLacetEditor = function($timeout) {
    return {
        restrict: "E",
        replace: true,
        transclude: false,
        controller: function ($scope,$element, $attrs ,$transclude ) {
            angular.element(document).ready(function() {
                $timeout(function(){
                    window.lacetEditor.init($element);
                    return function (scope, element, attrs, controller) {

                    }
                });
            });
        }
    }
};
var angularLacetViewer = function($timeout) {
    return {
        restrict: "E",
        replace: true,
        transclude: false,
        controller: function ($scope,$element, $attrs ,$transclude ) {
            angular.element(document).ready(function() {
                $timeout(function(){
                    window.lacetViewer.init($element,$scope);
                });
            });
        }
    }
};