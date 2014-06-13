( function($) {
    var local   = false; 
    var order_id = null;
    var order_base_url = "http://mourningwarning.corp.ne1.yahoo.com/tbvm/php/proxy.php?url=http://plannedwithstand.corp.sg3.yahoo.com:4080/hack/ws/";
    var order_list_url = "http://mourningwarning.corp.ne1.yahoo.com/tbvm/php/proxy.php?url=http://plannedwithstand.corp.sg3.yahoo.com:4080/hack/ws/list";
    var order_data = null;
    var order_list = null;
    var dimension = null;
    var dimensions = null;
    var chart_confs = { "m_c": "Mobile Click", "a_c": "Mobile and PC: Click", 
    "m_v": "Mobile View", "a_v": "Mobile and PC: View"}; 

    $.getKeys = function( obj) {
      var keys = [], name;
      for (key in obj) {
        if (obj.hasOwnProperty(key)) {
          keys.push(key)
        }
      } 
      return keys;
    }

    $.reloadOrderData = function ( obj) {
      var dimensions = $.getKeys(obj);
      console.info(obj)
      if( !dimension ){
        dimension = dimensions[0];
      }
      $.loadDimensions(dimensions);
      $.reloadChart(dimension);
    }

    $.loadDimensions = function( dimensions) {
      $('#dp-event').empty();
      for(var i in dimensions){
        $('#dp-event').append(
            $('<li>').append(
              $('<button>').attr('class','btn btn-default btn-block').attr('value',dimensions[i]).text(dimensions[i])))
      }
      $('#dp-event > li > button').click(function(e){
        dimension = this.value;
        $.reloadChart(dimension);
      });
    }
      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      $.drawPieChart = function (chart_type , matrix) {

      // Create the data table.
      var data = new google.visualization.DataTable();
      // Set chart options
      var options = {'chartArea':{'left':20,'top':0,'width':"100%",'height':"100%"},
                     'width':400,
                     'height':300};
      data.addColumn('string', 'Topping');
      data.addColumn('number', 'Slices');
      var rows = [];
      for(var key in matrix){
        var tmp = [];
        tmp.push(key);
        tmp.push(parseInt(matrix[key]));
        rows.push(tmp);
      }

      data.addRows(rows);

      var chart = new google.visualization.PieChart(document.getElementById(chart_type));
      chart.draw(data, options);
    }
    
    $.reloadChart = function(dimension) {
      var matrix_all = order_data[dimension];
// Load the Visualization API and the piechart package.

      for(var type in matrix_all){
        console.info(type);
        $.drawPieChart(type, matrix_all[type]);
      }
      $('#title').text(dimension + " Distribution" + " in:" + order_list[order_id] );
  }
    $.init = function () {
      $.getOrderList();
      $.getOrderData(order_id);

      google.load('visualization', '1.0', {'packages':['corechart']});
    };

    $.getOrderData = function ( order_id) {
      $.ajax( {
        url: order_base_url + order_id,
        //url: "getdata_io_" + order_id,
        success: function( data) {
          order_data = data;
        //  order_data = '{ "age": { "MA" : { "10-20": 100, "20-30": 200 } }, "edu": { "MA" : { "10-20": 100, "20-30": 200 } } }'
          $.reloadOrderData( order_data);
        }
      } );
    };
    $.loadOrderList = function(order_list) {
      $('#dp-campaign').empty();
      for(var i in order_list){
        if(!order_id) {
          order_id = i;
        }
        $('#dp-campaign').append(
            $('<li>').append(
              $('<button>').attr('class','btn btn-default btn-block').attr('value',i).text(order_list[i])))
      }
      $('#dp-campaign> li > button').click(function(e){
        order_id = this.value;
        $.getOrderData(order_id);
      });
      $.getOrderData(order_id);
    }
    $.getOrderList = function () {
      $.ajax( {
        url: order_list_url,
        //url: "order_list",
        success: function( data) {
          //order_list = jQuery.parseJSON( data );
          order_list = data;
          $.loadOrderList( order_list);
        }
      } );
    };
})(jQuery);
