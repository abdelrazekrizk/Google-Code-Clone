<?php

require_once(sfConfig::get('sf_lib_dir').'/filter/base/BaseFormFilterPropel.class.php');

/**
 * Items filter form base class.
 *
 * @package    epgp
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormFilterGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseItemsFormFilter extends BaseFormFilterPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'name'        => new sfWidgetFormFilterInput(),
      'playerid'    => new sfWidgetFormPropelChoice(array('model' => 'Roster', 'add_empty' => true)),
      'raidid'      => new sfWidgetFormPropelChoice(array('model' => 'Raids', 'add_empty' => true)),
      'itemid'      => new sfWidgetFormFilterInput(),
      'baseval'     => new sfWidgetFormFilterInput(),
      'inflatedval' => new sfWidgetFormFilterInput(),
    ));

    $this->setValidators(array(
      'name'        => new sfValidatorPass(array('required' => false)),
      'playerid'    => new sfValidatorPropelChoice(array('required' => false, 'model' => 'Roster', 'column' => 'id')),
      'raidid'      => new sfValidatorPropelChoice(array('required' => false, 'model' => 'Raids', 'column' => 'id')),
      'itemid'      => new sfValidatorSchemaFilter('text', new sfValidatorInteger(array('required' => false))),
      'baseval'     => new sfValidatorSchemaFilter('text', new sfValidatorNumber(array('required' => false))),
      'inflatedval' => new sfValidatorSchemaFilter('text', new sfValidatorNumber(array('required' => false))),
    ));

    $this->widgetSchema->setNameFormat('items_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'Items';
  }

  public function getFields()
  {
    return array(
      'id'          => 'Number',
      'name'        => 'Text',
      'playerid'    => 'ForeignKey',
      'raidid'      => 'ForeignKey',
      'itemid'      => 'Number',
      'baseval'     => 'Number',
      'inflatedval' => 'Number',
    );
  }
}
