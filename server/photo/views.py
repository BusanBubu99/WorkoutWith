from http.client import HTTPResponse
from django.contrib import messages
from django.shortcuts import render
from django.views.generic.list import ListView
from django.views.generic.edit import UpdateView, CreateView, DeleteView
from django.views.generic.detail import DetailView
from .models import Photo
from django.shortcuts import redirect
from django.shortcuts import HttpResponseRedirect

# Create your views here.

'''
1
app_name 설정을 통해 namespace(이름공간)확보
다른 앱들과 url pattern 이름이 겹치는 것을 방지하기 위해 사용한다.
2
path(url pattern, view, url pattern name),
함수형 뷰는 이름만 적으면 되고 클래스형 뷰는 이름.as_view()
name은 나중에 불러쓰기 위해 지정한다.
3
url pattern
<int:pk>
해당 작성에 대한 번호로 이동해야지 상세페이지 및 그 글에 대한 삭제, 수정을 할 수 있다.
4
views 경로 지정
해당 url이 들어오면 views의 해당 view의 로직을 따라서 처리한다.
5
name
네임 설정을 통해 이후 클릭하면 해당 url로 이동하도록 설정한다.
'''
class PhotoList(ListView):
    model = Photo
    template_name_suffix = '_list'

class PhotoCreate(CreateView):
    model = Photo
    fields = ['text','image']
    template_name_suffix = '_create'
    success_url = '/'

    def form_valid(self, form):
        form.instance.author_id = self.request.user.id
        if form.is_valid():
            form.instance.save()
            return redirect('/')
        else:
            return self.render_to_response({'form': form})

class PhotoUpdate(UpdateView):
    model = Photo
    fields = ['text', 'image']
    template_name_suffix = '_update'
    # success_url = '/'

    def dispatch(self, request, *args, **kwargs):
        object = self.get_object()
        if object.author != request.user:
            messages.waring(request, '수정할 권한이 없습니다.')
            return HttpResponseRedirect('/')
        else:
            return super(PhotoUpdate, self).dispatch(request, *args, **kwargs)


class PhotoDelete(DeleteView):
    model = Photo
    template_name_suffix = '_delete'
    success_url = '/'

    def dispatch(self, request, *args, **kwargs):
        object = self.get_object()
        if object.author != request.user:
            messages.warning(request, '삭제할 권한이 없습니다.')
            return HttpResponseRedirect('/')
        else:
            return super(PhotoDelete, self).dispatch(request, *args, **kwargs)

class PhotoDetail(DetailView):
    model = Photo
    template_name_suffix = '_detail'
