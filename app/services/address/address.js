'use strict';

//  Martialarts Address Service
catwalkApp.factory('MartialartsAddress', ['MartialartsBaseService',function (MartialartsBaseService) {
    var entityUrl = MartialartsBaseService.getEntityUrl('address');
    return MartialartsBaseService.getResource(entityUrl,{},{
        'columns':{method: 'POST', params:{},url:entityUrl + 'columns'},
        'api':{method: 'POST', params:{},url:entityUrl + 'api'},
        'schema':{method: 'POST', params:{},url:entityUrl + 'schema'}
    });
}
]);
