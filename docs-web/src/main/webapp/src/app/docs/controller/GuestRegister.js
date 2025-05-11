'use strict';

angular.module('docs').controller('GuestRegister', function(Restangular, $scope,  $state, $dialog, $translate) {
    $scope.req = {
        username: '',
        email: '',
        password: ''
    };
    $scope.register = function() {
        if (!$scope.req.username || !$scope.req.email || !$scope.req.password) {
            var title = $translate.instant('guestregister.error_title');
            var msg = $translate.instant('guestregister.error_message');
            var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
            $dialog.messageBox(title, msg, btns).open();
            return;
        }
        Restangular.one('user/request').post('', $scope.req).then(function() {
            var title = $translate.instant('guestregister.success_title');
            var msg = $translate.instant('guestregister.success_message');
            var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
            $dialog.messageBox(title, msg, btns).open().then(function () {
                $state.go('login');
            });
        }, function () {
            var title = $translate.instant('guestregister.error_title');
            var msg = $translate.instant('guestregister.error_message');
            var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
            $dialog.messageBox(title, msg, btns);
        });

    };
});