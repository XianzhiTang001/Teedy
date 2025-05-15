'use strict';

angular.module('docs').controller('Register', function(Restangular, $scope, $rootScope, $state, $dialog, $translate) {
    // Get the app configuration
    Restangular.one('app').get().then(function(data) {
        $rootScope.app = data;
    });

    $scope.req = {
        username: '',
        email: '',
        password: ''
    };
    $scope.register = function() {
        if (!$scope.req.username || !$scope.req.email || !$scope.req.password) {
            var title = $translate.instant('register.error_title');
            var msg = $translate.instant('register.info_error_message');
            var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
            $dialog.messageBox(title, msg, btns);
            return;
        }
        Restangular.one('user/request').post('', $scope.req).then(function() {
            var title = $translate.instant('register.success_title');
            var msg = $translate.instant('register.success_message');
            var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
            $dialog.messageBox(title, msg, btns).then(function () {
                $state.go('login');
            });
        }, function () {
            var title = $translate.instant('register.error_title');
            var msg = $translate.instant('register.server_error_message');
            var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
            $dialog.messageBox(title, msg, btns);
        });

        $scope.goToLogin = function () {
            $state.go('login');
        };
    };
});