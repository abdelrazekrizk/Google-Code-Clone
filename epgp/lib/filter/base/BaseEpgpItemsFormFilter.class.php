<?php

require_once(sfConfig::get('sf_lib_dir').'/filter/base/BaseFormFilterPropel.class.php');

/**
 * EpgpItems filter form base class.
 *
 * @package    epgp
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormFilterGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseEpgpItemsFormFilter extends BaseFormFilterPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'name'        => new sfWidgetFormFilterInput(),
      'awarded_to'  => new sfWidgetFormPropelChoice(array('model' => 'EpgpRoster', 'add_empty' => true)),
      'awarded_at'  => new sfWidgetFormPropelChoice(array('model' => 'EpgpRaids', 'add_empty' => true)),
      'itemid'      => new sfWidgetFormFilterInput(),
      'baseval'     => new sfWidgetFormFilterInput(),
      'inflatedval' => new sfWidgetFormFilterInput(),
    ));

    $this->setValidators(array(
      'name'        => new sfValidatorPass(array('required' => false)),
      'awarded_to'  => new sfValidatorPropelChoice(array('required' => false, 'model' => 'EpgpRoster', 'column' => 'id')),
      'awarded_at'  => new sfValidatorPropelChoice(array('required' => false, 'model' => 'EpgpRaids', 'column' => 'id')),
      'itemid'      => new sfValidatorSchemaFilter('text', new sfValidatorInteger(array('required' => false))),
      'baseval'     => new sfValidatorSchemaFilter('text', new sfValidatorNumber(array('required' => false))),
      'inflatedval' => new sfValidatorSchemaFilter('text', new sfValidatorNumber(array('required' => false))),
    ));

    $this->widgetSchema->setNameFormat('epgp_items_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'EpgpItems';
  }

  public function getFields()
  {
    return array(
      'id'          => 'Number',
      'name'        => 'Text',
      'awarded_to'  => 'ForeignKey',
      'awarded_at'  => 'ForeignKey',
      'itemid'      => 'Number',
      'baseval'     => 'Number',
      'inflatedval' => 'Number',
    );
  }
}
