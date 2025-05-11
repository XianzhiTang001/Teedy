'use strict';

angular.module('docs').controller('AdminRequests', function(Restangular, $scope,  $state, $dialog, $translate) {
    $scope.requests = [];

    $scope.loadRequests = function() {
        Restangular.one('user/request').get().then(function(data) {
            $scope.requests = data;
        }, function() {
            var title = $translate.instant('adminrequests.load_error_title');
            var msg = $translate.instant('adminrequests.load_error_message');
            var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
            $dialog.messageBox(title, msg, btns);
        });
    };

    $scope.approve = function(request) {
        var title = $translate.instant('adminrequests.approve_title');
        var msg = $translate.instant('adminrequests.approve_message', { username: request.username });
        var btns = [
            { result: 'cancel', label: $translate.instant('cancel') },
            { result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary' }
        ];
        $dialog.messageBox(title, msg, btns, function(result) {
            if (result === 'ok') {
                Restangular.one('user/request', request.id).post('approve').then(function() {
                    var title = $translate.instant('adminrequests.approve_success_title');
                    var msg = $translate.instant('adminrequests.approve_success_message', { username: request.username });
                    var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
                    $dialog.messageBox(title, msg, btns);
                    $scope.loadRequests();
                }, function() {
                    var title = $translate.instant('adminrequests.approve_error_title');
                    var msg = $translate.instant('adminrequests.approve_error_message', { username: request.username });
                    var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
                    $dialog.messageBox(title, msg, btns);
                });
            }
        });

    };

    $scope.reject = function(request) {
        var title = $translate.instant('adminrequests.reject_title');
        var msg = $translate.instant('adminrequests.reject_message', { username: request.username });
        var btns = [
            { result: 'cancel', label: $translate.instant('cancel') },
            { result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary' }
        ];
        $dialog.messageBox(title, msg, btns, function(result) {
            if (result === 'ok') {
                Restangular.one('user/request', request.id).post('reject').then(function() {
                    var title = $translate.instant('adminrequests.reject_success_title');
                    var msg = $translate.instant('adminrequests.reject_success_message', { username: request.username });
                    var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
                    $dialog.messageBox(title, msg, btns);
                    $scope.loadRequests();
                }, function() {
                    var title = $translate.instant('adminrequests.reject_error_title');
                    var msg = $translate.instant('adminrequests.reject_error_message', { username: request.username });
                    var btns = [{result: 'ok', label: $translate.instant('ok'), cssClass: 'btn-primary'}];
                    $dialog.messageBox(title, msg, btns);
                });
            }
        });

    };

});