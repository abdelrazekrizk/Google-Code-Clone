class DecaysController < ApplicationController
  before_filter :login_required, :except => %w[index show]
  def index
    @decays = Decay.find(:all, :order => 'performed_on DESC')
  end
  
  def show
    @decay = Decay.find(params[:id])
  end
  
  def adminindex
    @decays = Decay.find(:all, :order => 'performed_on DESC')
  end
  
  def new
  end
  
  def create
    d = Decay.new
    if(d.update_attributes(params[:decay]))
      flash[:notice] = "Decayed successfully"
    else
      flash[:error] = "Could not decay."
    end
    redirect_to :action => :adminindex
  end
  
  def destroy
    if(Decay.find(params[:id]).destroy)
      flash[:notice] = "Decay destroyed successfully."
    else
      flash[:notice] = "Decay could not be destroyed."
    end
    redirect_to :action => :adminindex
  end
end
