'use strict';

//  Martialarts Tuition Service
catwalkApp.factory('MartialartsTuition', ['MartialartsBaseService',function (MartialartsBaseService) {
    var entityUrl = MartialartsBaseService.getEntityUrl('tuition');
    return MartialartsBaseService.getResource(entityUrl,{},{
        'columns':{method: 'POST', params:{},url:entityUrl + 'columns'},
        'api':{method: 'POST', params:{},url:entityUrl + 'api'},
        'schema':{method: 'POST', params:{},url:entityUrl + 'schema'}
    });
}
]);
