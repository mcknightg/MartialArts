'use strict';

//  Martialarts Attendance Service
catwalkApp.factory('MartialartsAttendance', ['MartialartsBaseService',function (MartialartsBaseService) {
    var entityUrl = MartialartsBaseService.getEntityUrl('attendance');
    return MartialartsBaseService.getResource(entityUrl,{},{
        'columns':{method: 'POST', params:{},url:entityUrl + 'columns'},
        'api':{method: 'POST', params:{},url:entityUrl + 'api'},
        'schema':{method: 'POST', params:{},url:entityUrl + 'schema'}
    });
}
]);
