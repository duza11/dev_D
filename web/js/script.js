$(function () {
    var click_flg = true;
    var frm_cnt = 0;

    $(".form-block[id^='form_block']").each(function (idx, obj) {
        if ($(obj).attr('id') != 'form_block[0]') {
            frm_cnt++;
        }
    });

    $(document).on('click', '.add', function () {
        if (click_flg) {
            click_flg = false;
            var original = $('#form_block\\[' + frm_cnt + '\\]');

            frm_cnt++;

            original
                    .clone()
                    .hide()
                    .insertAfter(original)
                    .attr('id', 'form_block[' + frm_cnt + ']') // クローンのid属性を変更。
                    .end() // 一度適用する
                    .find('input, select').each(function (idx, obj) {
                $(obj).attr({
                    id: $(obj).attr('id').replace(/\[[0-9]+\]$/, '[' + frm_cnt + ']'),
                    name: $(obj).attr('name').replace(/\[[0-9]+\]$/, '[' + frm_cnt + ']')
                });
                if ($(obj).attr('type') == 'text' || $(obj).attr('type') == 'number') {
                    $(obj).val('');
                }
                $(obj).find(":selected").prop("selected", false);
            });

            // clone取得
            var clone = $('#form_block\\[' + frm_cnt + '\\]');
            clone.find('.ac-btn').children('div.add-btn').hide();
            clone.find('.ac-btn').children('div.close-btn').show();
            clone.slideDown('fast', function () {
                click_flg = true;
            });
        }
    });

    $(document).on('click', '.close-btn', function () {
        var removeObj = $(this).parent().parent().parent();
        removeObj.slideUp('fast', function () {
            removeObj.remove();
            // 番号振り直し
            frm_cnt = 0;
            $(".form-block[id^='form_block']").each(function (idx, obj) {
                if ($(obj).attr('id') != 'form_block[0]') {
                    frm_cnt++;
                    $(obj)
                            .attr('id', 'form_block[' + frm_cnt + ']') // id属性を変更。
                            .find('input, select').each(function (idx, obj) {
                        $(obj).attr({
                            id: $(obj).attr('id').replace(/\[[0-9]+\]$/, '[' + frm_cnt + ']'),
                            name: $(obj).attr('name').replace(/\[[0-9]+\]$/, '[' + frm_cnt + ']')
                        });
                    });
                }
            });
        });
    });

    $('#submit_select').change(function () {
        if ($('#submit_form option:selected').val() != '-1') {
            $('#submit_form').submit();
        }
    });

    $('.alert').fadeIn(1000).delay(500).fadeOut(2000);
    
    $(document).on("click", "#clear", function () {
        $('input,textarea')
                .not('input[type=\"radio\"],[type=\"checkbox\"],:hidden, :button, :submit,:reset')
                .val('');
        $('input[type=\"radio\"],[type=\"checkbox\"],select')
                .removeAttr('checked')
                .find(":selected").prop("selected", false);
    })
});